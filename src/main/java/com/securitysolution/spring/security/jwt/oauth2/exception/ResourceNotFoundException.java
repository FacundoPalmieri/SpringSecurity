package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

    private Long id;

    public ResourceNotFoundException(String message, Long id) {
      super(message);
      this.id = id;
    }
}
