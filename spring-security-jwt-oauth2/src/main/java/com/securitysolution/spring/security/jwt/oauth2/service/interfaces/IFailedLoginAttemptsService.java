package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

public interface IFailedLoginAttemptsService {

    Integer get();
    void update(Integer failedLoginAttempts);


}
