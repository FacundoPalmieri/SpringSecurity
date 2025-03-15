package com.securitysolution.spring.security.jwt.oauth2.dto;

import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author [Facundo Palmieri]
 */
@Data
public class RefreshTokenDTO {

    private String token;

    @NotNull(message = "exception.refreshToken.refreshEmpty")
    private String refreshToken;

    @NotNull(message = "exception.refreshToken.userEmpty")
    private String user;
}
