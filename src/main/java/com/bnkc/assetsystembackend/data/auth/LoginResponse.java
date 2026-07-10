package com.bnkc.assetsystembackend.data.auth;

import com.bnkc.assetsystembackend.data.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(description = "JWT authentication response")
public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        long expiresIn,
        UserDto user
) {
}
