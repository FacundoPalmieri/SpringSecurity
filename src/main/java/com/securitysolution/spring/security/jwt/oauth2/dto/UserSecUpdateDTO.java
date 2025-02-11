package com.securitysolution.spring.security.jwt.oauth2.dto;

import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserSecUpdateDTO {

    @NotNull(message = "userSecCreateDTO.id.empty")
    private Long id;

    private Boolean enabled;
    private Set<Long> rolesList = new HashSet<>();




}
