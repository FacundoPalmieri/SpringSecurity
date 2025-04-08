package com.securitysolution.spring.security.jwt.oauth2.dto;



import jakarta.validation.constraints.NotNull;

public record FailedLoginAttemptsRequestDTO(
        @NotNull(message = "FailedLoginAttemptsDTO.value.empty")
        Integer value) {
}
