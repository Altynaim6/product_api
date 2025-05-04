package com.example.product_api.service;

import com.example.product_api.model.dto.auth.AuthRequest;
import com.example.product_api.model.dto.auth.AuthResponse;
import com.example.product_api.model.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse login(AuthRequest request);

    AuthResponse register(RegisterRequest request);
}