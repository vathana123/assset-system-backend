package com.bnkc.assetsystembackend.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {
    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid username or password.");
    }

    public InvalidCredentialsException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
