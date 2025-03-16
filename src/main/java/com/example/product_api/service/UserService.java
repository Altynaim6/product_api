package com.example.product_api.service;

import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> all(int page, int size);
    UserResponse findById(Long id);
    UserResponse update(Long id, UserRequest request);
    UserResponse save(UserRequest request);
    void deleteById(Long id);
}
