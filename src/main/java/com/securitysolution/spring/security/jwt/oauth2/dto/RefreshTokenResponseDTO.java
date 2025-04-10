package com.securitysolution.spring.security.jwt.oauth2.dto;

import lombok.Data;

@Data
public class RefreshTokenResponseDTO {

    private String jwt;

    private String refreshToken;

    private Long idUser;

    private String username;
}
