package com.jobis.tax.domain.user.repository;

import com.jobis.tax.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByToken(String refreshToken);
    Optional<RefreshToken> findByToken(String refreshToken);
}
