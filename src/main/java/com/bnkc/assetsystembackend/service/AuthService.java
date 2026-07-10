package com.bnkc.assetsystembackend.service;

import com.bnkc.assetsystembackend.data.auth.ChangePasswordRequest;
import com.bnkc.assetsystembackend.data.auth.LoginRequest;
import com.bnkc.assetsystembackend.data.auth.LoginResponse;
import com.bnkc.assetsystembackend.data.auth.RefreshTokenRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);

    void changePassword(ChangePasswordRequest request);
}
