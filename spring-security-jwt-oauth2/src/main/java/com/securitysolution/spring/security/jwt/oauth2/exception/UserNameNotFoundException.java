package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;

@Getter
public class UserNameNotFoundException extends RuntimeException {
    private String username;

    public UserNameNotFoundException(String username) {
        this.username = username;
    }









}
