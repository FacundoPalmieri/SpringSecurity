package com.securitysolution.spring.security.jwt.oauth2.exception;

/**
 * Excepción personalizada para manejar errores cuando no se encuentra un la configuración del  Token
 * <p>
 * Se lanza cuando se intenta acceder a los valores de configuración y los mismos están nulos.
 * </p>
 */
public class TokenConfigNotFound extends RuntimeException {
    /** Identificador de la entidad asociada al permiso no encontrado. */
    private  Long id;

    /** Tipo de entidad a la que pertenece el permiso (Ejemplo: UsuarioService). */
    private  String entityType;

    /** Operación que intentó realizar el usuario (Ejemplo: Save, Update). */
    private  String operation;

    /** Constructor que inicializa la excepción con un mensaje y detalles sobre la entidad y operación.*/
    public TokenConfigNotFound(Long id, String entityType, String operation) {

        this.id = id;
        this.entityType = entityType;
        this.operation = operation;
    }

}