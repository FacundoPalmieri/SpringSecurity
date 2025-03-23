package com.securitysolution.spring.security.jwt.oauth2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author [Facundo Palmieri]
 */
@Data
public class RefreshTokenDTO {

    private String jwt;

    @NotNull(message = "refreshTokenDTO.refreshEmpty")
    private String refreshToken;

    @NotNull(message = "refreshTokenDTO.userIdEmpty")
    private Long user_id;

    @NotNull(message = "refreshTokenDTO.usernameEmpty")
    private String username;

}
