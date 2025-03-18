package com.example.product_api.controller;

import com.example.product_api.model.dto.user.UserRequest;
import com.example.product_api.model.dto.user.UserResponse;
import com.example.product_api.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("John Doe");
        userResponse.setEmail("john.doe@example.com");
        userResponse.setRole("USER");
    }

    @Test
    void all() throws Exception {
        Mockito.when(userService.all(0, 10)).thenReturn(List.of(userResponse));

        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void findById() throws Exception {
        Mockito.when(userService.findById(1L)).thenReturn(userResponse);

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void update() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Updated Name");
        userRequest.setEmail("updated.email@example.com");
        userRequest.setPassword("securepassword");
        userRequest.setRole("ADMIN");

        UserResponse updatedResponse = new UserResponse();
        updatedResponse.setId(1L);
        updatedResponse.setName("Updated Name");
        updatedResponse.setEmail("updated.email@example.com");
        updatedResponse.setRole("ADMIN");

        Mockito.when(userService.update(anyLong(), any(UserRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated.email@example.com"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void save() throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("New User");
        userRequest.setEmail("new.user@example.com");
        userRequest.setPassword("password123");
        userRequest.setRole("USER");

        UserResponse savedResponse = new UserResponse();
        savedResponse.setId(2L);
        savedResponse.setName("New User");
        savedResponse.setEmail("new.user@example.com");
        savedResponse.setRole("USER");

        Mockito.when(userService.save(any(UserRequest.class))).thenReturn(savedResponse);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("new.user@example.com"));
    }

    @Test
    void deleteById() throws Exception {
        Mockito.doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isOk());
    }
}