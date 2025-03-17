package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.model.RefreshToken;

/**
 * @author [Facundo Palmieri]
 */
public interface IRefreshTokenService {

    RefreshToken createRefreshToken(String username);

    boolean validateRefreshToken(String refreshToken, String username);

    void deleteRefreshTokenByUsername(String token,String username);

    RefreshToken getRefreshTokenByUsername(String token, String username);

}
