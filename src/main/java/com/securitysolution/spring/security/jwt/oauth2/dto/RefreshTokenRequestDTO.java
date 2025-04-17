package com.securitysolution.spring.security.jwt.oauth2.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class RefreshTokenRequestDTO {


    @NotNull(message = "refreshTokenRequestDTO.refreshEmpty")
    private String refreshToken;

    @NotNull(message = "refreshTokenRequestDTO.userIdEmpty")
    private Long user_id;

    @NotNull(message = "refreshTokenRequestDTO.usernameEmpty")
    private String username;

}
