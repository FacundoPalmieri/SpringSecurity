package com.securitysolution.spring.security.jwt.oauth2.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RefreshTokenConfigDTO(
    @NotNull
    @Min(value = 1, message = "refreshTokenConfigDTO.invalidExpiration")
    Long expiration
){}
