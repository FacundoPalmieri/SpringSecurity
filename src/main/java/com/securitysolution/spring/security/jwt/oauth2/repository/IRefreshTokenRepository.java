package com.securitysolution.spring.security.jwt.oauth2.repository;

import com.securitysolution.spring.security.jwt.oauth2.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author [Facundo Palmieri]
 */
public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String token);
}
