package com.securitysolution.spring.security.jwt.oauth2.exception;

import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }





    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<Response<String>> handleUsernameNotFoundException(UserNameNotFoundException ex) {


        // Cargar el mensaje de error desde properties
        String logMessage = messageSource.getMessage(
                "exception.UsernameNotFound.log", // La clave de la propiedad
                new Object[]{ex.getUsername()}, // Pasamos el username como parámetro
                LocaleContextHolder.getLocale() // Usamos el locale para la localización
        );

        log.error(logMessage);


        // Crear mensaje genérico para el usuario
        String userMessage = messageSource.getMessage(
                "exception.UsernameNotFound.user",
                null,
                LocaleContextHolder.getLocale()
        );

        // Crear la respuesta con el mensaje personalizado
        Response<String> response = new Response<>(false, userMessage, null);

        // Crear la respuesta con el mensaje personalizado
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

    }

    // Manejo de excepción por validaciones de anotaciones en los DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)  // Esta excepción se lanza cuando hay una violación de validación de un objeto (por ejemplo, DTO)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Crear un mapa para almacenar los errores de validación
        Map<String, String> errors = new HashMap<>();

        // Iteramos sobre los errores de cada campo que falló en la validación
        ex.getBindingResult().getFieldErrors().forEach(error ->{
            String errorMessage = messageSource.getMessage(error.getDefaultMessage(), null, LocaleContextHolder.getLocale());   // Guardamos el nombre del campo (error.getField()) y el mensaje de error correspondiente (error.getDefaultMessage()) en el mapa
            errors.put(error.getField(), errorMessage);

            //Se guarda Log
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (authentication != null) ? authentication.getName() : "Anónimo";

            String logMessage = messageSource.getMessage("exception.validation.log",new Object[]{error.getField(),errorMessage,username}, LocaleContextHolder.getLocale());
            log.error(logMessage);
        });


        // Devolvemos una respuesta con un código de estado HTTP 400 (Bad Request) y el mapa de errores como cuerpo de la respuesta
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)  // Establecemos el estado HTTP a 400 (petición incorrecta)
                .body(errors);  // Enviamos los errores de validación como cuerpo de la respuesta
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

        //Se construye en el mensaje para el usuario.
        String userMessage = messageSource.getMessage("exception.database.user", null, LocaleContextHolder.getLocale());

        //Se construye en el mensaje para log.
        String logMessage = messageSource.getMessage(
                "exception.database.log",
                new Object[]{ ex.getEntityType(),  ex.getEntityId(), ex.getEntityName(), ex.getOperation(), ex.getRootCause(), userMessage},
                LocaleContextHolder.getLocale()
                );

        log.error(logMessage);

        // Respuesta a usuario.
        Response<String> response = new Response<>(false, userMessage, null);
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

        // Cargar el mensaje de error para log.
        String logMessage = messageSource.getMessage(
                "exception.badCredentials.log", // La clave de la propiedad
                new Object[]{ex.getUsername()}, // Pasamos el username como parámetro
                LocaleContextHolder.getLocale() // Usamos el locale para la localización
        );

        log.error(logMessage);

        // Crear mensaje genérico para el usuario
        String userMessage = messageSource.getMessage(
                "exception.badCredentials.user",
                null,
                LocaleContextHolder.getLocale());

        // Crear la respuesta con el mensaje personalizado
        Response<String> response = new Response<>(false, userMessage, null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }




    @ExceptionHandler (PasswordMismatchException.class)
    public ResponseEntity<Response<String>> handlePasswordMismatchException(PasswordMismatchException ex) {

        //Se recupera usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userCreador = authentication.getName();

        //Construye mensaje para el LOG.
        String logMessage = messageSource.getMessage(
                "userService.save.passwordNotEquals.log",
                new Object[]{ ex.getUserNuevo(),userCreador},
                LocaleContextHolder.getLocale());

        //Log Error.
        log.error(logMessage);

        //Se construye mensaje para usuario.
        String userMessage = messageSource.getMessage("userService.save.passwordNotEquals.user", null, LocaleContextHolder.getLocale());

        Response<String> response = new Response<>(false, userMessage, null);
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);

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
