package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.data.auth.*;
import com.bnkc.assetsystembackend.data.dto.UserDto;
import com.bnkc.assetsystembackend.entity.UserInfo;
import com.bnkc.assetsystembackend.exception.InvalidCredentialsException;
import com.bnkc.assetsystembackend.exception.ValidationException;
import com.bnkc.assetsystembackend.mapper.UserMapper;
import com.bnkc.assetsystembackend.repository.UserRepository;
import com.bnkc.assetsystembackend.config.security.JwtService;
import com.bnkc.assetsystembackend.config.security.UserAuthorityMapper;
import com.bnkc.assetsystembackend.service.AuthService;
import com.bnkc.assetsystembackend.service.CurrentUserService;
import com.bnkc.assetsystembackend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final String TOKEN_TYPE = "Bearer";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final CurrentUserService currentUserService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        } catch (AuthenticationException ex) {
            throw new InvalidCredentialsException();
        }

        UserInfo user = userRepository.findByUsernameWithRolesAndPermissions(request.username())
                .orElseThrow(InvalidCredentialsException::new);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user);

        return toLoginResponse(user, accessToken, refreshToken);
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(RefreshTokenRequest request) {
        RefreshTokenService.RefreshTokenRotation rotation = refreshTokenService.rotateRefreshToken(request.refreshToken());
        String accessToken = jwtService.generateAccessToken(rotation.user());

        return toLoginResponse(rotation.user(), accessToken, rotation.refreshToken());
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        if (!Objects.equals(request.newPassword(), request.confirmPassword())) {
            throw new ValidationException("New password and confirm password do not match.");
        }

        UserInfo user = currentUserService.getCurrentUser();

        if (!passwordEncoder.matches(request.oldPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Old password is incorrect.");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPassword())) {
            throw new ValidationException("New password must be different from the old password.");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        user.setChangedPasswordAt(LocalDateTime.now());
        userRepository.save(user);
        refreshTokenService.revokeAllUserTokens(user);
    }

    private LoginResponse toLoginResponse(UserInfo user, String accessToken, String refreshToken) {

        return new LoginResponse(
                accessToken,
                refreshToken,
                TOKEN_TYPE,
                jwtService.getAccessTokenExpiresInSeconds(),
                toUserResponse(user)
        );
    }

    private UserDto toUserResponse(UserInfo user) {

        return userMapper.toDto(user);
    }
}
