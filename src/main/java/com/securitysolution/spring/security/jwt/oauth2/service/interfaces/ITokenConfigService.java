package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

/**
 * Interfaz que define los métodos para el servicio de gestión de expiración de tokens.
 * Proporciona métodos para obtener y actualizar la expiración de un token.
 */
public interface ITokenConfigService {

    /**
     * Obtiene la duración de expiración del token en milisegundos.
     * @return La duración de expiración del token en milisegundos.
     */
    Long getExpiration();

    /**
     * Actualiza la duración de expiración del token.
     * @param milliseconds El nuevo valor de la duración de expiración del token en milisegundos.
     */
    int updateExpiration(Long milliseconds);
}
