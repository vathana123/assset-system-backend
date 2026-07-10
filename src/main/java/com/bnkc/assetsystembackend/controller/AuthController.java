package com.bnkc.assetsystembackend.controller;

import com.bnkc.assetsystembackend.data.auth.ChangePasswordRequest;
import com.bnkc.assetsystembackend.data.auth.LoginRequest;
import com.bnkc.assetsystembackend.data.auth.LoginResponse;
import com.bnkc.assetsystembackend.data.auth.RefreshTokenRequest;
import com.bnkc.assetsystembackend.service.AuthService;
import com.bnkc.assetsystembackend.service.LogoutHandleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@Tag(name = "Authentication", description = "Authentication and token management APIs")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login with username and password")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "Rotate a valid refresh token and issue a new access token")
    public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/change-password")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Change the current user's password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok(Map.of("message", "Password changed successfully."));
    }
}
