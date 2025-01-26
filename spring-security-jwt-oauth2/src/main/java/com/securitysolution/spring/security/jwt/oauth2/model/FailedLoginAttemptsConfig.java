package com.securitysolution.spring.security.jwt.oauth2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
@Table(name="intentos_fallidos")
public class FailedLoginAttemptsConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "valor", nullable = false)
    private int value;
}
