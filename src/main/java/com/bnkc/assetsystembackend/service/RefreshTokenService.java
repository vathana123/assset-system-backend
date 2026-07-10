package com.bnkc.assetsystembackend.service;

import com.bnkc.assetsystembackend.entity.UserInfo;

public interface RefreshTokenService {
    String createRefreshToken(UserInfo user);

    RefreshTokenRotation rotateRefreshToken(String refreshToken);

    void revokeAllUserTokens(UserInfo user);

    record RefreshTokenRotation(UserInfo user, String refreshToken) {
    }
}
