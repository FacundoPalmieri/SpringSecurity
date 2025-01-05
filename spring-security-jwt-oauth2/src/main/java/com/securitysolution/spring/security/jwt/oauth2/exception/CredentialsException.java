package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;

@Getter
public class CredentialsException extends RuntimeException {

    private final String username;


    public CredentialsException(String username) {
        this.username = username;
    }
}