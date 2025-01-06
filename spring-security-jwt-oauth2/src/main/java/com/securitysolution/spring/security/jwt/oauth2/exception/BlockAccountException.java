package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;

@Getter
public class BlockAccountException extends RuntimeException {
    private final Long id;
    private final String username;

    public BlockAccountException(String message, Long id, String username) {
        super(message);
        this.id = id;
        this.username = username;
    }
}
