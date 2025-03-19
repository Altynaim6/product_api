package com.example.product_api.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionTest {

    private CustomException customException;

    @BeforeEach
    void setUp() {
        customException = new CustomException("Test exception", HttpStatus.BAD_REQUEST);
    }

    @Test
    void testToString() {
        String expected = "CustomException [status=400 BAD_REQUEST, message=Test exception]";

        String result = customException.toString();

        assertEquals(expected, result);
    }

    @Test
    void getStatus() {
        HttpStatus status = customException.getStatus();

        assertEquals(HttpStatus.BAD_REQUEST, status);
    }

    @Test
    void setStatus() {
        customException.setStatus(HttpStatus.NOT_FOUND);

        assertEquals(HttpStatus.NOT_FOUND, customException.getStatus());
    }

    @Test
    void testCustomExceptionMessage() {
        String expectedMessage = "Test exception";

        String result = customException.getMessage();

        assertEquals(expectedMessage, result);
    }
}