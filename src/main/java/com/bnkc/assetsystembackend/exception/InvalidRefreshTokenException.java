package com.bnkc.assetsystembackend.exception;

import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends ApiException {
    public InvalidRefreshTokenException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid refresh token.");
    }

    public InvalidRefreshTokenException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
