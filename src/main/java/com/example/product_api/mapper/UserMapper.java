package com.example.product_api.mapper;


import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;

import java.util.List;

public interface UserMapper {
    User toUser(User user, UserRequest request);
    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);
}