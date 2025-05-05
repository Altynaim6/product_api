package com.example.product_api.service.impl;

import com.example.product_api.exception.CustomException;
import com.example.product_api.mapper.UserMapper;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.service.TwoFactorAuthService;
import com.example.product_api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TwoFactorAuthService twoFactorAuthService;

    @Override
    public List<UserResponse> all(int page, int size) {
        return userMapper.toResponseList(
                userRepository.findAll(PageRequest.of(page, size)).getContent()
        );
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        // Check if the new email is used by another user
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email already taken by another user", HttpStatus.CONFLICT);
        }

        return userMapper.toResponse(
                userRepository.save(userMapper.toUser(user, request))
        );
    }

    @Override
    public UserResponse save(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("User already exists", HttpStatus.CONFLICT);
        }

        return userMapper.toResponse(
                userRepository.save(userMapper.toUser(new User(), request))
        );
    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Включить 2FA
    public void enable2FA(String email, String secretKey) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        user.setTwoFaSecret(secretKey);
        userRepository.save(user);
    }

    // Проверка кода
    public boolean verify2FACode(String email, int code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));
        return twoFactorAuthService.verifyCode(user.getTwoFaSecret(), code);
    }


}
