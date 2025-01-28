package com.securitysolution.spring.security.jwt.oauth2.dto;

import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class UserSecDTO {
    @NotBlank(message = "userSecDTO.username.empty")
    @Email(message = "userSecDTO.username.email")
    private String username;

    @NotBlank(message = "userSecDTO.password.empty")
    @Size(min = 10, message = "userSecDTO.password.min")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{10,}",
            message = "userSecDTO.password.pattern")
    private String password1;

    @NotBlank(message = "userSecDTO.password.empty")
    @Size(min = 10, message = "userSecDTO.password.min")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{10,}",
            message = "userSecDTO.password.pattern")
    private String password2;

    @NotNull(message = "userSecDTO.password.role")
    private Set<Role> rolesList;
}

