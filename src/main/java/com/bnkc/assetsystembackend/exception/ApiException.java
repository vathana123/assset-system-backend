package com.bnkc.assetsystembackend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message); // 🔥 IMPORTANT
        this.status = status;
    }
}
