package com.example.product_api.service.impl;

import com.example.product_api.exception.CustomException;
import com.example.product_api.mapper.UserMapper;
import com.example.product_api.model.domain.User;
import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.repository.UserRepository;
import com.example.product_api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public List<UserResponse> all(int page, int size) {
        return userMapper.toResponseList(userRepository.findAll(PageRequest.of(page, size)).getContent());
    }

    @Override
    public UserResponse findById(Long id) {
        return userMapper.toResponse(userRepository.findById(id).orElseThrow(() -> new CustomException("User Not Found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("User Not Found", HttpStatus.NOT_FOUND));
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("User already exists", HttpStatus.CONFLICT);
        }
        return userMapper.toResponse(userRepository.save(userMapper.toUser(user, request)));
    }

    @Override
    public UserResponse save(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("User already exists", HttpStatus.CONFLICT);
        }
        return userMapper.toResponse(userRepository.save(userMapper.toUser(new User(), request)));
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}