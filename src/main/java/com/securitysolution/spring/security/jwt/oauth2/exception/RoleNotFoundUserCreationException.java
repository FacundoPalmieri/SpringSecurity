package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleNotFoundUserCreationException extends RuntimeException {
  private Long id;
  private String role;
  private  String entityType;  // Tipo de la entidad (CursosService, Tema, etc.)
  private  String operation; // Operación (Ejemplo: Save, update)

  public RoleNotFoundUserCreationException(String message, Long id, String role, String entityType, String operation) {
    super(message);
    this.id = id;
    this.role = role;
    this.entityType = entityType;
    this.operation = operation;
  }
}
