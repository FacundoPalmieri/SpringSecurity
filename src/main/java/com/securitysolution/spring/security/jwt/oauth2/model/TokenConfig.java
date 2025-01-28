package com.securitysolution.spring.security.jwt.oauth2.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "token_config")
public class TokenConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="Expiracion",  nullable = false)
    private Long expiration;
}
