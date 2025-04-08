package com.securitysolution.spring.security.jwt.oauth2.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionRequestDTO {

    @NotNull(message = "permissionDTO.name.empty")
    private String permissionName;
}
