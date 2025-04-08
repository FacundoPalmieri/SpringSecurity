package com.securitysolution.spring.security.jwt.oauth2.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record RefreshTokenConfigRequestDTO(
    @NotNull
    @Min(value = 1, message = "refreshTokenConfigDTO.invalidExpiration")
    Long expiration
){}
