package com.securitysolution.spring.security.jwt.oauth2.dto;

import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class RoleDTO {


    @NotBlank(message = "roleDTO.role.empty")
    private String role;

    @NotNull(message = "roleDTO.permission.empty")
    private Set <Permission> permissionsList;
}
