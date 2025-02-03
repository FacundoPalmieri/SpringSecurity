package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleExistingException extends RuntimeException {
    private  String entityType;  // Tipo de la entidad (CursosService, Tema, etc.)
    private  String operation; // Operación (Ejemplo: Save, update)
    private String role;
    public RoleExistingException(String message, String entityType, String operation, String role) {
        super(message);
        this.entityType = entityType;
        this.operation = operation;
        this.role = role;
    }
}
