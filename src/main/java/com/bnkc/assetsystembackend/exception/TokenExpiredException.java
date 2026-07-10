package com.bnkc.assetsystembackend.exception;

import org.springframework.http.HttpStatus;

public class TokenExpiredException extends ApiException {
    public TokenExpiredException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
