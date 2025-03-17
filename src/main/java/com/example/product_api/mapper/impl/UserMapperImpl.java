package com.example.product_api.mapper.impl;

import com.example.product_api.mapper.UserMapper;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.model.enums.Role;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(User user, UserRequest request) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        // Safely set role if it's valid
        if (request.getRole() != null) {
            try {
                user.setRole(Role.valueOf(request.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role: " + request.getRole());
            }
        }
        return user;
    }

    @Override
    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole() != null ? user.getRole().toString() : null);
        return response;
    }

    @Override
    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}