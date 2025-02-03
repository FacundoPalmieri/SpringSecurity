package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Se utiliza para métodos findbyId y retorna un 404
 */
@Getter
@Setter
public class UserNotFoundException extends RuntimeException {
  private  String entityType;  // Tipo de la entidad (CursosService, Tema, etc.)
  private  String operation; // Operación (Ejemplo: Save, update)
  private Long id;

  public UserNotFoundException(String message,String entityType,String operation,Long id) {

    super(message);
    this.entityType = entityType;
    this.operation = operation;
    this.id = id;

  }
}
