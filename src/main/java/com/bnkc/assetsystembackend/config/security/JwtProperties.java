package com.bnkc.assetsystembackend.config.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(
        @NotBlank(message = "JWT issuer is required")
        String issuer,

        @NotBlank(message = "JWT secret is required")
        String secret,

        @Positive(message = "Access token expiration must be positive")
        long accessTokenExpirationMs,

        @Positive(message = "Refresh token expiration must be positive")
        long refreshTokenExpirationMs
) {
}
