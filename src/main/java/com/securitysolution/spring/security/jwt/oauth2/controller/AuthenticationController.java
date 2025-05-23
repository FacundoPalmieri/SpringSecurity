package com.securitysolution.spring.security.jwt.oauth2.controller;

import com.securitysolution.spring.security.jwt.oauth2.dto.*;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IUserService;
import com.securitysolution.spring.security.jwt.oauth2.service.UserDetailsServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Controlador de autenticación que maneja los endpoints relacionados con el inicio de sesión,
 * la solicitud de restablecimiento de contraseña y el restablecimiento efectivo de la contraseña del usuario.
 *
 * Este controlador expone las siguientes operaciones:
 * <ul>
 *     <li><b>/api/auth/login</b>: Inicia sesión autenticando las credenciales del usuario y devuelve un token en caso de autenticación exitosa.</li>
 *     <li><b>/api/auth/token/refresh</b>: Actualiza el refresh token de un usuario.</li>
 *     <li><b>/api/auth/logout</b>: cierra la sesión del usuario eliminado el refresh token.</li>
 *     <li><b>/api/auth/password/reset-request</b>: Solicita el restablecimiento de la contraseña enviando un correo con una URL de redireccionamiento.</li>
 *     <li><b>/api/auth/password/reset-request</b>: Solicita el restablecimiento de la contraseña enviando un correo con una URL de redireccionamiento.</li>
 *     <li><b>/api/auth/password/reset</b>: Restablece la contraseña del usuario utilizando el token recibido en el correo electrónico.</li>
 * </ul>
 *
 * El controlador depende de los servicios {@link UserDetailsServiceImp} para la autenticación de usuario y
 * {@link IUserService} para la gestión de las contraseñas y el envío de correos electrónicos de restablecimiento.
 */
@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private IUserService userService;


    /**
     * Inicia sesión autenticando las credenciales del usuario.
     *
     * @param userRequest Objeto que contiene las credenciales del usuario.
     * @return ResponseEntity con:
     *      <ul>
     *          <li><b>200 OK</b>: Autenticado.</li>
     *          <li><b>401 Unauthorized</b>: No autenticado.</li>
     *          <li><b>403 Forbidden</b>: Cuenta bloqueada o sin permisos de acceso.</li>
     *      </ul>
     */
    @Operation(summary = "Autenticación de usuario", description = "Recibe las credenciales del usuario y devuelve un token en caso de autenticación exitosa.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticado existosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "Cuenta bloqueada o sin permisos de acceso.")
    })
    @PostMapping("/login")
    public ResponseEntity<Response<AuthLoginResponseDTO>> login(@RequestBody @Valid AuthLoginRequestDTO userRequest) {
        Response<AuthLoginResponseDTO> response = this.userDetailsService.loginUser(userRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Endpoint para actualizar el refresh token de un usuario.
     *
     * <p>Este método valída el refresh token. Si el mismo es valido, lo actualiza y crear un nuevo jwt. El resultado se devuelve en una
     *  respuesta HTTP con el nuevo refresh token y el JWT. Recibe en el cuerpo de la solicitud el refresh token.
     *</p>
     * @param refreshTokenRequestDTO El objeto de transferencia de datos que contiene el ID del usuario,
     *                        el nombre de usuario y el token de refresco a actualizar. El objeto
     *                        debe ser válido y será validado automáticamente.
     * @return ResponseEntity con:
     *      <ul>
     *          <li><b>200 OK</b>: Actualización del refresh token y nuevo jwt.</li>
     *          <li><b>401 Unauthorized</b>: No autenticado.</li>
     *          <li><b>403 Forbidden</b>: Cuenta bloqueada o sin permisos de acceso.</li>
     *      </ul>
     */

    @Operation(summary = "refrescar el token de autenticación de un usuario", description = "Recibe refresh token en cuerpo de la solicitud, valida el token y genera uno nuevo junto con un nuevo JWT (JSON Web Token)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualización del refresh token y nuevo jwt"),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "Cuenta bloqueada o sin permisos de acceso.")
    })
    @PostMapping("/token/refresh")
    public ResponseEntity<Response<RefreshTokenResponseDTO>> refreshToken(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO) {
        Response<RefreshTokenResponseDTO>response = userDetailsService.refreshToken(refreshTokenRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Cierra la sesión del usuario invalidando el refresh token.
     * Este método elimina el refresh token del usuario y lo invalida, lo que evita que pueda ser utilizado para obtener nuevos JWTs.

     * @param refreshTokenRequestDTO El objeto que contiene el refresh token, el JWT, el ID del usuario y el email.
     * @return ResponseEntity con:
     *     <ul>
     *      ><b>200 OK</b>: Cierre de sesión correcto.</li>
     *      <li><b>401 Unauthorized</b>: No autenticado.</li>
     *      <li><b>403 Forbidden</b>: Cuenta bloqueada o sin permisos de acceso.</li>
     *    </ul>
     */
    @Operation(summary = "Elimina el refresh token", description = "elimina el refresh token del usuario actual para invalidar futuras solicitudes de actualización del token.Recibe un objeto `refreshTokenRequestDTO` que contiene el JWT, el código del refresh token, el ID del usuario y el email.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "cierre de sesión exitoso"),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "Cuenta bloqueada o sin permisos de acceso.")
    })
    @DeleteMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Response<String>> logout(@RequestBody @Valid RefreshTokenRequestDTO refreshTokenRequestDTO) {
        Response<String> response = userDetailsService.logout(refreshTokenRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }




    /**
     *  Solicita el restablecimiento de la contraseña enviando un correo con una URL de redireccionamiento.
     *
     * @param email  Correo electrónico del usuario al cual se enviará el enlace para restablecer la contraseña.
     * @return ResponseEntity con:
     *      <ul>
     *          <li><b>200 OK</b>: Correo enviado exitosamente.</li>
     *      </ul>
     */
    @Operation(summary = "Solicitud de restablecimiento de password", description = "Envía un correo electrónico al usuario con un enlace para restablecer la contraseña.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Correo enviado exitosamente.")
    })
    @PostMapping("/password/reset-request")
    public ResponseEntity<Response<String>> requestResetPassword(@RequestParam String email) {
        Response<String> response = userService.createTokenResetPasswordForUser(email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Restablece la contraseña del usuario utilizando el token recibido en el correo electrónico.
     *
     * @param resetPasswordRequestDTO Objeto que contiene el nuevo password del usuario y el token recibido por correo electrónico.
     * @param request Solicitud HTTP que contiene detalles de la petición
     * @return ResponseEntity con:
     *      <ul>
     *          <li><b>200 OK</b>: Restablecimiento de contraseña exitoso.</li>
     *      </ul>
     */
    @Operation(summary = "Restablecimiento de la contraseña.", description = "Restablece la contraseña del usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Restablecimiento de contraseña exitoso.")
    })
    @PostMapping("/password/reset")
    public ResponseEntity<Response<String>> resetPassword(@Valid @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO, HttpServletRequest request) {
        Response <String> response = userService.updatePassword(resetPasswordRequestDTO, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}





