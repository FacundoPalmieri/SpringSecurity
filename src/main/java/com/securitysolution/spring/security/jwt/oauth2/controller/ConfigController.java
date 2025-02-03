package com.securitysolution.spring.security.jwt.oauth2.controller;

import com.securitysolution.spring.security.jwt.oauth2.dto.FailedLoginAttemptsDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.MessageDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.TokenConfigDTO;
import com.securitysolution.spring.security.jwt.oauth2.model.MessageConfig;
import com.securitysolution.spring.security.jwt.oauth2.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev")
@PreAuthorize("hasRole('DEV')")
public class ConfigController {
    @Autowired
    private ConfigService configService;


    /**
     * Obtiene la configuración de mensajes.
     * <p>
     * Requiere rol <b>DEV</b> para acceder.
     * </p>
     *
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Listado de mensajes recuperado exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         </ul>
     */
    @Operation(summary = "Obtener configuración de mensajes", description = "Obtiene la configuración de mensajes.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de mensajes recuperado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
    })
    @GetMapping("/message/get")
    public ResponseEntity<Response<List<MessageConfig>>> getMessage() {
        Response<List<MessageConfig>> response = configService.getMessage();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * Actualiza la configuración de un mensaje.
     * <p>
     * Requiere rol <b>DEV</b> para acceder.
     * </p>
     *
     * @param messageDTO Objeto con los datos del mensaje a actualizar.
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Mensaje actualizado exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         <li><b>404 Not Found</b>: Mensaje no encontrado para actualizar.</li>
     *         </ul>
     */
    @Operation(summary = "Actualizar mensaje", description = "Actualiza la configuración de un mensaje.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mensaje actualizado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Mensaje no encontrado para actualizar.")
    })
    @PatchMapping("/message/update")
    public ResponseEntity<Response<MessageConfig>> updateMessage(@Valid @RequestBody MessageDTO messageDTO) {
        Response<MessageConfig> response =  configService.updateMessage(messageDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }




    /**
     * Obtiene la cantidad de intentos fallidos de sesión.
     * <p>
     * Requiere el rol <b>DEV</b> para acceder.
     * </p>
     *
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Cantidad de intentos fallidos recuperada exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         </ul>
     */
    @Operation(summary = "Obtener intentos fallidos de sesión", description = "Obtiene la cantidad de intentos fallidos de sesión.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cantidad de intentos fallidos recuperada exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso.")
    })
    @GetMapping("/session/get")
    public ResponseEntity<Response<Integer>> getAttempts() {
        Response<Integer> response =  configService.getAttempts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Actualiza la cantidad de intentos fallidos de sesión.
     * <p>
     * Requiere el rol <b>DEV</b> para acceder.
     * </p>
     *
     * @param failedLoginAttemptsDTO Datos para actualizar los intentos fallidos de sesión.
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Cantidad de intentos fallidos actualizada exitosamente.</li>
     *         <li><b>400 Bad Resquest</b>: Datos inválidos para actualizar la cantidad de intentos fallidos de sesión.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         </ul>
     */
    @Operation(summary = "Actualizar intentos fallidos de sesión", description = "Actualiza la cantidad de intentos fallidos de sesión.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cantidad de intentos fallidos actualizada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para actualizar la cantidad de intentos fallidos de sesión."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso.")
    })
    @PatchMapping("/session/update")
    public ResponseEntity<Response<Integer>> updateAttempts(@Valid @RequestBody FailedLoginAttemptsDTO failedLoginAttemptsDTO) {
        Response<Integer> response = configService.updateAttempts(failedLoginAttemptsDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Obtiene la expiración del token.
     * <p>
     * Requiere el rol <b>DEV</b> para acceder.
     * </p>
     *
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Expiración del token recuperada exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         </ul>
     */
    @Operation(summary = "Obtener expiración del token", description = "Obtiene la expiración del token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expiración del token recuperada exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso.")
    })
    @GetMapping ("token/get")
    public ResponseEntity<Response<Long>> getTokenExpiration() {
        Response<Long> response = configService.getTokenExpiration();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Actualiza la expiración del token.
     * <p>
     * Requiere el rol <b>DEV</b> para acceder.
     * </p>
     *
     * @param tokenConfigDTO Datos para actualizar la expiración del token.
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Expiración del token actualizada exitosamente.</li>
     *         <li><b>400 Bad Request</b>: Datos inválidos para actualizar la expiración del token.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         </ul>
     */
    @Operation(summary = "Actualizar expiración del token", description = "Actualiza la expiración del token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Expiración del token actualizada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para actualizar la expiración del token."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso.")
    })
    @PatchMapping("token/update")
    public ResponseEntity<Response<Long>> updateTokenExpiration(@Valid @RequestBody TokenConfigDTO tokenConfigDTO) {
         Response<Long> response = configService.updateTokenExpiration(tokenConfigDTO);
         return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
