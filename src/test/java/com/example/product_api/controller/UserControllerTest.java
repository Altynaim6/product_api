package com.example.product_api.controller;

import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testCreateUser() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Alice");
        userRequest.setEmail("alice@example.com");
        userRequest.setPassword("password123");
        userRequest.setRole("SELLER");

        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("Alice");
        userResponse.setEmail("alice@example.com");
        userResponse.setRole("SELLER");

        when(userService.save(userRequest)).thenReturn(userResponse);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }

    @Test
    public void testGetUserById() throws Exception {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("Alice");
        userResponse.setEmail("alice@example.com");
        userResponse.setRole("SELLER");

        when(userService.findById(1L)).thenReturn(userResponse);

        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)));
    }
}