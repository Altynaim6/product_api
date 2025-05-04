package com.example.product_api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 1) Load full security configuration
@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityFilterTest.TestConfig.class)
class SecurityFilterTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // wire in springSecurity() so @WithMockUser is honored
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void publicRegisterEndpoint_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/auth/register")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void adminEndpoint_shouldRedirectToLogin_whenUnauthenticated() throws Exception {
        mockMvc.perform(get("/admin/test")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminEndpoint_shouldReturn200_whenAdmin() throws Exception {
        mockMvc.perform(get("/admin/test")
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("admin"));
    }

    @Configuration
    static class TestConfig {
        @RestController
        static class DummyController {
            @GetMapping("/auth/register")
            String register() {
                return "public register";
            }
            @GetMapping("/admin/test")
            String admin() {
                return "admin";
            }
        }
    }
}