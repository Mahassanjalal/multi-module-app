package com.actora.userservice.mapper;

import com.actora.userservice.dto.CreateUserRequest;
import com.actora.userservice.dto.UpdateUserRequest;
import com.actora.userservice.dto.UserResponse;
import com.actora.userservice.entity.User;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for User entity and DTOs.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    /**
     * Convert CreateUserRequest to User entity.
     */
    User toEntity(CreateUserRequest request);

    /**
     * Convert User entity to UserResponse DTO.
     */
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    UserResponse toResponse(User user);

    /**
     * Convert list of User entities to list of UserResponse DTOs.
     */
    List<UserResponse> toResponseList(List<User> users);

    /**
     * Update User entity from UpdateUserRequest DTO.
     * Only non-null values will be updated.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(UpdateUserRequest request, @MappingTarget User user);
}
