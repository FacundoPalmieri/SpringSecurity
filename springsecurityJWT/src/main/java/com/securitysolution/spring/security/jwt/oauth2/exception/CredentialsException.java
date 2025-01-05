package com.todocodeacademy.springsecurity.exception;

import lombok.Getter;

@Getter
public class CredentialsException extends RuntimeException {

    private final String messageUser;

    public CredentialsException(String messageLog, String messageUser) {
        super(messageLog);
        this.messageUser = messageUser;
    }
}