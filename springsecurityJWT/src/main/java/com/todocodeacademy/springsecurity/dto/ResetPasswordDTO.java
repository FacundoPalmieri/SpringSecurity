package com.todocodeacademy.springsecurity.dto;

import jakarta.validation.constraints.NotBlank;

public record ResetPasswordDTO (@NotBlank String token, @NotBlank String newPassword){}
