package com.securitysolution.spring.security.jwt.oauth2.dto;

import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Data
public class RoleResponseDTO {

    private Long id;
    private String role;
    private Set<Permission> permissionsList = new HashSet<>();
}
