package com.securitysolution.spring.security.jwt.oauth2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO (
        @NotBlank
        String token,

        @NotBlank(message = "userSecDTO.password.empty")
        @Size(min = 10, message = "userSecDTO.password.min")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{10,}",
                message = "userSecDTO.password.pattern")
        String newPassword1,


        @NotBlank(message = "userSecDTO.password.empty")
        @Size(min = 10, message = "userSecDTO.password.min")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{10,}",
                message = "userSecDTO.password.pattern")
        @NotBlank
        String newPassword2) {}
