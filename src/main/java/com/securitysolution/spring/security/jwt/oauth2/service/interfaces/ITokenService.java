package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

public interface ITokenService {

    Long getExpiration();
    void updateExpiration(Long milliseconds);
}
