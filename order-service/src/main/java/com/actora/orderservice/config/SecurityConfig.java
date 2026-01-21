package com.actora.orderservice.config;

import com.actora.common.security.GatewayAuthenticationFilter;
import com.actora.common.security.SecurityConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public GatewayAuthenticationFilter gatewayAuthenticationFilter() {
        return new GatewayAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                    GatewayAuthenticationFilter gatewayAuthFilter) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints from common constants
                .requestMatchers(SecurityConstants.PUBLIC_ENDPOINTS).permitAll()
                .requestMatchers("/orders/health").permitAll()
                // Admin only endpoints
                .requestMatchers(HttpMethod.DELETE, "/orders/**").hasRole("ADMIN")
                .requestMatchers("/orders/statistics").hasAnyRole("ADMIN", "MANAGER")
                // Manager and Admin can update order status
                .requestMatchers(HttpMethod.PATCH, "/orders/*/status").hasAnyRole("ADMIN", "MANAGER")
                // All authenticated users can create and view orders
                .requestMatchers(HttpMethod.POST, "/orders/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/orders/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/orders/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(gatewayAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
