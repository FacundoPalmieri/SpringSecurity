package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleNotFoundException extends RuntimeException {
  private Long id;
  private String role;

  public RoleNotFoundException(String message, Long id, String role) {
    super(message);
    this.id = id;
    this.role = role;
  }
}
