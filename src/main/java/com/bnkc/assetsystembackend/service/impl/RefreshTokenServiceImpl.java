package com.bnkc.assetsystembackend.service.impl;

import com.bnkc.assetsystembackend.entity.RefreshToken;
import com.bnkc.assetsystembackend.entity.UserInfo;
import com.bnkc.assetsystembackend.exception.InvalidRefreshTokenException;
import com.bnkc.assetsystembackend.repository.RefreshTokenRepository;
import com.bnkc.assetsystembackend.repository.UserRepository;
import com.bnkc.assetsystembackend.config.security.JwtService;
import com.bnkc.assetsystembackend.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private static final int MAX_TOKEN_GENERATION_ATTEMPTS = 3;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public String createRefreshToken(UserInfo user) {
        for (int attempt = 0; attempt < MAX_TOKEN_GENERATION_ATTEMPTS; attempt++) {
            String refreshToken = jwtService.generateRefreshToken(user);
            String tokenHash = hashToken(refreshToken);

            if (!refreshTokenRepository.existsByTokenHash(tokenHash)) {
                persistRefreshToken(user, tokenHash);
                return refreshToken;
            }
        }

        throw new InvalidRefreshTokenException("Unable to create refresh token.");
    }

    @Override
    @Transactional
    public RefreshTokenRotation rotateRefreshToken(String refreshToken) {
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        String tokenHash = hashToken(refreshToken);
        RefreshToken storedToken = refreshTokenRepository.findByTokenHashAndRevokedAtIsNull(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        LocalDateTime now = LocalDateTime.now();
        if (!storedToken.getExpiresAt().isAfter(now)) {
            storedToken.setRevokedAt(now);
            refreshTokenRepository.save(storedToken);
            throw new InvalidRefreshTokenException("Refresh token has expired.");
        }

        UserInfo user = userRepository.findByUsernameWithRolesAndPermissions(jwtService.extractUsername(refreshToken))
                .orElseThrow(InvalidRefreshTokenException::new);

        String newRefreshToken = createRefreshToken(user);
        String newTokenHash = hashToken(newRefreshToken);

        storedToken.setLastUsedAt(now);
        storedToken.setRevokedAt(now);
        storedToken.setReplacedByTokenHash(newTokenHash);
        refreshTokenRepository.save(storedToken);

        return new RefreshTokenRotation(user, newRefreshToken);
    }

    @Override
    @Transactional
    public void revokeAllUserTokens(UserInfo user) {
        refreshTokenRepository.revokeAllActiveByUserId(user.getId(), LocalDateTime.now());
    }

    private void persistRefreshToken(UserInfo user, String tokenHash) {
        LocalDateTime now = LocalDateTime.now();

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .tokenHash(tokenHash)
                .expiresAt(now.plus(Duration.ofMillis(jwtService.getRefreshTokenExpiresInMillis())))
                .createdAt(now)
                .build();

        refreshTokenRepository.save(token);
    }

    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 algorithm is unavailable.", ex);
        }
    }
}
