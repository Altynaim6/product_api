package com.example.product_api.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalException {
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        return new ResponseEntity<>(e.getMessage(), e.getStatus());
    }
}
