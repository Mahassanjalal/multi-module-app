package com.actora.authservice.service;

import com.actora.authservice.client.UserServiceClient;
import com.actora.authservice.dto.*;
import com.actora.authservice.entity.AuthUser;
import com.actora.authservice.entity.RefreshToken;
import com.actora.authservice.entity.Role;
import com.actora.authservice.repository.AuthUserRepository;
import com.actora.authservice.repository.RefreshTokenRepository;
import com.actora.authservice.security.JwtService;
import com.actora.common.dto.ApiResponse;
import com.actora.common.exception.BusinessException;
import com.actora.common.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthUserRepository authUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        if (authUserRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        // Step 1: Create user profile in User Service
        UserServiceClient.CreateUserRequest userRequest = new UserServiceClient.CreateUserRequest(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPhone(),
                request.getAddress()
        );

        ApiResponse<UserServiceClient.UserDto> userResponse = userServiceClient.createUser(userRequest);

        Long userId = null;
        String fullName = request.getFirstName() + " " + request.getLastName();

        if (userResponse.isSuccess() && userResponse.getData() != null) {
            userId = userResponse.getData().id();
            fullName = userResponse.getData().fullName();
            log.info("User profile created in user-service with ID: {}", userId);
        } else {
            log.warn("Could not create user profile in user-service. Proceeding without user profile.");
        }

        // Step 2: Create auth user (stores only authentication data)
        AuthUser authUser = AuthUser.builder()
                .userId(userId)  // Link to user-service
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        authUser.addRole(Role.ROLE_USER);

        AuthUser savedUser = authUserRepository.save(authUser);
        log.info("Auth user created with ID: {}, linked to user-service ID: {}", savedUser.getId(), userId);

        return generateAuthResponse(savedUser, fullName);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getUsername());

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BusinessException("Invalid username or password", HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
        }

        AuthUser user = authUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND, "USER_NOT_FOUND"));

        // Revoke all existing refresh tokens
        refreshTokenRepository.revokeAllUserTokens(user);

        // Fetch user profile from user-service
        String fullName = fetchFullName(user);

        log.info("User logged in successfully: {}", user.getUsername());
        return generateAuthResponse(user, fullName);
    }

    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        log.info("Refreshing token");

        RefreshToken refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new BusinessException("Invalid refresh token", HttpStatus.UNAUTHORIZED, "INVALID_TOKEN"));

        if (!refreshToken.isValid()) {
            throw new BusinessException("Refresh token expired or revoked", HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED");
        }

        AuthUser user = refreshToken.getUser();

        // Revoke old refresh token
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        String fullName = fetchFullName(user);
        return generateAuthResponse(user, fullName);
    }

    @Transactional
    public void logout(String refreshToken) {
        log.info("Logging out user");

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElse(null);

        if (token != null) {
            refreshTokenRepository.revokeAllUserTokens(token.getUser());
        }
    }

    public TokenValidationResponse validateToken(String token) {
        try {
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.extractUsername(token);
                Long userId = jwtService.extractUserId(token);
                var roles = jwtService.extractRoles(token);

                return TokenValidationResponse.builder()
                        .valid(true)
                        .username(username)
                        .userId(userId)
                        .roles(roles)
                        .build();
            }
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
        }

        return TokenValidationResponse.builder()
                .valid(false)
                .build();
    }

    private String fetchFullName(AuthUser user) {
        if (user.getUserId() != null) {
            try {
                ApiResponse<UserServiceClient.UserDto> response = userServiceClient.getUserById(user.getUserId());
                if (response.isSuccess() && response.getData() != null) {
                    return response.getData().fullName();
                }
            } catch (Exception e) {
                log.warn("Could not fetch user profile: {}", e.getMessage());
            }
        }
        return user.getUsername(); // Fallback to username
    }

    private AuthResponse generateAuthResponse(AuthUser user, String fullName) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Save refresh token
        RefreshToken token = RefreshToken.builder()
                .token(refreshToken)
                .user(user)
                .expiryDate(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .build();
        refreshTokenRepository.save(token);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpiration() / 1000)
                .user(AuthResponse.UserInfo.builder()
                        .id(user.getUserId() != null ? user.getUserId() : user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .fullName(fullName)
                        .roles(user.getRoles().stream()
                                .map(Enum::name)
                                .collect(Collectors.toSet()))
                        .build())
                .build();
    }
}
