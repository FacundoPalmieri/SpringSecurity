package com.securitysolution.spring.security.jwt.oauth2.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDTO (@NotBlank String token, @NotBlank String newPassword){}
