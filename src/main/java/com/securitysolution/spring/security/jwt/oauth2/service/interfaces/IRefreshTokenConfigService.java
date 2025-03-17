package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.dto.RefreshTokenConfigDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;

/**
 * Interfaz que define los métodos para el servicio de gestión de expiración de Refresh tokens.
 * Proporciona métodos para obtener y actualizar la expiración del Refresh token.
 */
public interface IRefreshTokenConfigService {

    /**
     * Obtiene la duración de expiración del token en días.
     * @return La duración de expiración del token en días.
     */
    Long getExpiration();

    /**
     * Actualiza la expiración del Refresh Token en días
     * @param refreshTokenConfigDTO El nuevo valor de la duración de expiración del token en días.
     * @return El tiempo de expiración actualizado.
     */
    void updateExpiration (RefreshTokenConfigDTO refreshTokenConfigDTO);
}
