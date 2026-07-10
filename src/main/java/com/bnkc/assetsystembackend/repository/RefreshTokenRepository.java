package com.bnkc.assetsystembackend.repository;

import com.bnkc.assetsystembackend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHashAndRevokedAtIsNull(String tokenHash);

    boolean existsByTokenHash(String tokenHash);

    @Modifying
    @Query("""
            update RefreshToken token
            set token.revokedAt = :revokedAt
            where token.user.id = :userId and token.revokedAt is null
            """)
    int revokeAllActiveByUserId(@Param("userId") Long userId, @Param("revokedAt") LocalDateTime revokedAt);
}
