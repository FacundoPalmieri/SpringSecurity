package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageNotFoundException extends RuntimeException {
    private Long id;
    private  String entityType;  // Tipo de la entidad (CursosService, Tema, etc.)
    private  String operation; // Operación (Ejemplo: Save, update)
    public MessageNotFoundException(String message,Long id,  String entityType, String operation) {
        super(message);
        this.id = id;
        this.entityType = entityType;
        this.operation = operation;

    }
}
