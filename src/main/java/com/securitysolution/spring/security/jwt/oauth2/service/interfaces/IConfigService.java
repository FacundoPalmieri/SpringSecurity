package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.dto.*;
import com.securitysolution.spring.security.jwt.oauth2.model.MessageConfig;
import org.springframework.http.ResponseEntity;
import java.util.List;

/**
 * Interfaz que define los métodos para la gestión de configuraciones del sistema.
 * Proporciona métodos para obtener y actualizar configuraciones relacionadas con mensajes,
 * intentos de inicio de sesión y expiración de tokens.
 */
public interface IConfigService {
    /**
     * Obtiene la lista de configuraciones de mensajes.
     * @return Una respuesta que contiene una lista de objetos {@link MessageConfig} con la configuración de mensajes.
     */
    Response<List<MessageConfig>> getMessage();

    /**
     * Actualiza la configuración de un mensaje.
     * @param messageDto El objeto {@link MessageDTO} que contiene los detalles del mensaje a actualizar.
     * @return Una respuesta que contiene el objeto {@link MessageConfig} actualizado.
     */
    Response<MessageConfig> updateMessage(MessageDTO messageDto);


    /**
     * Obtiene el número de intentos fallidos de inicio de sesión permitidos.
     * @return Una respuesta que contiene el número máximo de intentos de inicio de sesión.
     */
    Response<Integer> getAttempts();

    /**
     * Actualiza el número de intentos fallidos de inicio de sesión permitidos.
     *
     * @param failedLoginAttemptsDTO El objeto {@link FailedLoginAttemptsDTO} que contiene la nueva configuración de intentos fallidos.
     * @return Una respuesta que contiene el número actualizado de intentos permitidos.
     */
    Response<Integer> updateAttempts(FailedLoginAttemptsDTO failedLoginAttemptsDTO);

    /**
     * Obtiene la duración en minutos del tiempo de expiración del token de autenticación.
     * @return Una respuesta que contiene la duración del token en minutos.
     */
    Response<Long> getTokenExpiration();

    /**
     * Actualiza la duración de expiración del token de autenticación.
     *
     * @param tokenConfigDTO El objeto {@link TokenConfigDTO} que contiene la nueva configuración de expiración del token.
     * @return Una respuesta que contiene el nuevo valor de expiración del token en minutos.
     */
    Response<Long> updateTokenExpiration(TokenConfigDTO tokenConfigDTO);


    /**
     * Obtiene la duración en días del tiempo de expiración del Refresh Token
     * @return Una respuesta que contiene la duración del token en días
     */
    Response<Long> getRefreshTokenExpiration();


    /**
     * Actualiza la duración de expiración del Refresh Token
     * @param refreshTokenConfigDTO
     * @return Una respuesta que contiene el nuevo valor de expiración del token en días.
     */
    Response<Long> updateRefreshTokenExpiration(RefreshTokenConfigDTO refreshTokenConfigDTO);


}
