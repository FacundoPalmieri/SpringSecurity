package com.securitysolution.spring.security.jwt.oauth2.exception;

import lombok.Getter;

@Getter
public class DataBaseException extends RuntimeException {

    private final String entityType;  // Tipo de la entidad (Curso, Tema, etc.)
    private final Long entityId;      // ID de la entidad (Curso, Tema, etc.)
    private final String entityName;  // Nombre de la entidad (por ejemplo, "Curso de Java")
    private final String operation; // Operación (Ejemplo: Save, update)


    // Constructor con detalles de la entidad y causa raíz
    public DataBaseException(Throwable cause, String entityType, Long entityId, String entityName, String operation) {
        super(cause);
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityName = entityName;
        this.operation = operation;
    }



    public String getRootCause() {
        return getCause() != null ? getCause().toString() : "Desconocida";
    }

}