package com.securitysolution.spring.security.jwt.oauth2.dto;



import jakarta.validation.constraints.NotNull;

public record FailedLoginAttemptsDTO(
        @NotNull(message = "FailedLoginAttemptsDTO.value.empty")
        Integer value) {
}
