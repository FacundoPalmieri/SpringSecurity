package com.securitysolution.spring.security.jwt.oauth2.dto;

import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author [Facundo Palmieri]
 */
@Data
public class RefreshTokenDTO {

    private String token;

    @NotNull(message = "refreshTokenDTO.refreshEmpty")
    private String refreshToken;

    @NotNull(message = "refreshTokenDTO.userEmpty")
    private String user;
}
