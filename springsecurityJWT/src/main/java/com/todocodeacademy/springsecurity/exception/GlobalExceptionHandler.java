package com.todocodeacademy.springsecurity.exception;

import com.todocodeacademy.springsecurity.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Maneja las excepciones de tipo {@link DataBaseException}.
     * Esta excepción se lanza cuando ocurre un error específico relacionado con la persistencia en la base de datos.
     *
     * @param ex La excepción lanzada cuando ocurre un error en la base de datos.
     * @return Una respuesta HTTP con el código de estado 500 (INTERNAL_SERVER_ERROR) y el mensaje de error.
     */
    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<Response<String>> handleDataBaseException(DataBaseException ex) {
        // Loguear el error para diagnóstico
        log.error("Error al acceder a la base de datos: [ENTIDAD: {}] - [ID {}] -  [NOMBRE:{}] - [OPERACIÓN:{}] -  [CAUSA RAÍZ: {}] - [MENSAJE USUARIO: {}]",
                ex.getEntityType(),  ex.getEntityId(), ex.getEntityName(), ex.getOperation(), ex.getRootCause(), ex.getMessage());

        // Mensaje para el usuario final
        Response<String> response = new Response<>(false, ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Manejo de 404 Not Found
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response<String>> handleNotFound(Exception ex) {
        String messageUser = messageSource.getMessage("exception.notFound", null, LocaleContextHolder.getLocale());
        Response<String> response = new Response<>(false, messageUser, null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    // Manejo de Acceso Denegado
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Response<String>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {

        // Obtener información del usuario autenticado, si existe
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "Anónimo";

        // Obtener la URL del endpoint al que se intentó acceder
        String requestedUrl = request.getRequestURI();

        // Obtener el mensaje desde el archivo de propiedades y asignar datos
        String logMessage = messageSource.getMessage("exception.accessDenied.log", new Object[]{username, requestedUrl, ex.getMessage()}, LocaleContextHolder.getLocale());

        // Loguear la excepción para detalles de diagnóstico
        log.error(logMessage, ex);

        // Mensaje para el usuario final
        String messageUser = messageSource.getMessage("exception.accessDenied.user", null, LocaleContextHolder.getLocale());
        Response<String> response = new Response<>(false, messageUser, null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }





    // Manejo de credenciales invalidad
    @ExceptionHandler({CredentialsException.class})
    public ResponseEntity<Response<String>> handleCredentialsException(CredentialsException ex) {

        log.error(ex.getMessage());

        // Crear la respuesta con el mensaje personalizado
        Response<String> response = new Response<>(false, ex.getMessageUser(), null);

        // Crear la respuesta con el mensaje personalizado
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }






    /**
     * Maneja todas las excepciones no previstas en los controladores y servicios.
     * Esta excepción sirve como un manejo genérico de cualquier error no controlado previamente.
     *
     * @param e La excepción no controlada que se ha lanzado.
     * @return Una respuesta HTTP con el código de estado 500 (INTERNAL_SERVER_ERROR) y un mensaje genérico de error.
     */


    // Manejo de exception Generales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<String>> handleGeneralException(Exception e) {
        // Loguear la excepción para detalles de diagnóstico
        log.error("Error inesperado: " + e.getMessage(), e);

        // Respuesta genérica para cualquier excepción no capturada
        String messageUser = messageSource.getMessage("exception.generic", null, LocaleContextHolder.getLocale());
        Response<String> response = new Response<>(false, messageUser, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }





}
