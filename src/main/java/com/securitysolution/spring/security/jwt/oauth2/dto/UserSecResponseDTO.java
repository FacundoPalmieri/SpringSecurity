package com.securitysolution.spring.security.jwt.oauth2.dto;

import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class UserSecResponseDTO {

    private Long id;
    private String username;
    private Set<Role> rolesList;
}
