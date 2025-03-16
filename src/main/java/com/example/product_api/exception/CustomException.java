package com.example.product_api.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends RuntimeException {
    private HttpStatus status;

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    @Override
    public String toString() {
        return "CustomException [status=" + status + ", message=" + getMessage() + "]";
    }
}

