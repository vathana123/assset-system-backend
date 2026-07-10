package com.bnkc.assetsystembackend.config.security;

import com.bnkc.assetsystembackend.entity.UserInfo;
import com.bnkc.assetsystembackend.exception.TokenExpiredException;
import com.bnkc.assetsystembackend.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String TOKEN_TYPE_CLAIM = "token_type";
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private static final int MINIMUM_HMAC_KEY_BYTES = 32;

    private final JwtProperties properties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties properties) {
        this.properties = properties;
        this.signingKey = createSigningKey(properties.secret());
    }

    public String generateAccessToken(UserInfo user) {
        return buildToken(
                user,
                Map.of(
                        TOKEN_TYPE_CLAIM, ACCESS_TOKEN_TYPE,
                        "userId", user.getId(),
                        "roles", UserAuthorityMapper.roleNames(user),
                        "permissions", UserAuthorityMapper.permissionNames(user)
                ),
                properties.accessTokenExpirationMs()
        );
    }

    public String generateRefreshToken(UserInfo user) {
        return buildToken(
                user,
                Map.of(
                        TOKEN_TYPE_CLAIM, REFRESH_TOKEN_TYPE,
                        "userId", user.getId()
                ),
                properties.refreshTokenExpirationMs()
        );
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public boolean validateAccessToken(String token, UserDetails userDetails) {
        return validateToken(token, userDetails) && ACCESS_TOKEN_TYPE.equals(extractTokenType(token));
    }

    public boolean isRefreshToken(String token) {
        return REFRESH_TOKEN_TYPE.equals(extractTokenType(token));
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (TokenExpiredException ex) {
            return true;
        }
    }

    public long getAccessTokenExpiresInSeconds() {
        return Duration.ofMillis(properties.accessTokenExpirationMs()).toSeconds();
    }

    public long getRefreshTokenExpiresInMillis() {
        return properties.refreshTokenExpirationMs();
    }

    private String buildToken(UserInfo user, Map<String, Object> claims, long expirationMs) {
        Instant now = Instant.now();

        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .issuer(properties.issuer())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .id(UUID.randomUUID().toString())
                .signWith(signingKey)
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String extractTokenType(String token) {
        return extractClaim(token, claims -> claims.get(TOKEN_TYPE_CLAIM, String.class));
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException("JWT token has expired.");
        } catch (JwtException | IllegalArgumentException ex) {
            throw new UnauthorizedException("Invalid JWT token.");
        }
    }

    private SecretKey createSigningKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);

        if (keyBytes.length < MINIMUM_HMAC_KEY_BYTES) {
            throw new IllegalStateException("JWT secret must be at least 256 bits when Base64 decoded.");
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
