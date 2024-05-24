package com.myweb.mamababy.repositories;

import com.myweb.mamababy.models.BlacklistedToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    Optional<BlacklistedToken> findByToken(String token);

    @Transactional
    void deleteAllByExpirationDateBefore(Date date);
}
