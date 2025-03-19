package com.example.product_api.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GlobalExceptionTest.TestController.class)
class GlobalExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @RestController
    @RequestMapping("/test")
    static class TestController {
        @GetMapping("/error")
        public void throwError() {
            throw new CustomException("Test Exception", HttpStatus.NOT_FOUND);
        }
    }

    @Test
    void handleCustomException() throws Exception {
        mockMvc.perform(get("/test/error"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Test Exception"));
    }
}