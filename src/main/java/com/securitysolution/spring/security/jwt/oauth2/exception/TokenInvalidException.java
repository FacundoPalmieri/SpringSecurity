package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;

@Getter
public class TokenInvalidException extends RuntimeException {
    private final String username;

    public TokenInvalidException(String message, String username) {

        super(message);
        this.username = username;
    }
}
