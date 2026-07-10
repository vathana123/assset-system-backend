package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.entity.UserInfo;
import com.bnkc.assetsystembackend.service.CurrentUserService;
import com.bnkc.assetsystembackend.service.LogoutHandleService;
import com.bnkc.assetsystembackend.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutHandleServiceImpl implements LogoutHandleService {
    private final CurrentUserService currentUserService;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        UserInfo currentUser = currentUserService.getCurrentUser();
        refreshTokenService.revokeAllUserTokens(currentUser);
        SecurityContextHolder.clearContext();
    }
}
