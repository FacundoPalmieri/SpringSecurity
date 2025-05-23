package com.securitysolution.spring.security.jwt.oauth2.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Entidad que representa el Refresh token.
 */
@Entity
@Data
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String refreshToken;

    @OneToOne(targetEntity = UserSec.class)
    @JoinColumn(name = "user_id", nullable = false)
    private UserSec user;

    private LocalDateTime expirationDate;

    private LocalDateTime createdDate;

}
