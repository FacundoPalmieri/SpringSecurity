package com.securitysolution.spring.security.jwt.oauth2.exception;

import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IMessageService messageService;



    @ExceptionHandler({TokenInvalidException.class})
    public ResponseEntity<Response> handleTokenInvalidException(TokenInvalidException ex, HttpServletRequest request) {
        //Obtener IP
        String ip = request.getRemoteAddr();

        //Cargar mensaje para el log.
        String logMessage = messageService.getMessage("exception.validateToken.log",new Object[]{ex.getUsername(),ip}, LocaleContextHolder.getLocale());

        log.error(logMessage, ex);

        //Cargar mensaje para el usuario.
        String userMessage = messageService.getMessage("exception.validateToken.user", null, LocaleContextHolder.getLocale());

        //Construir respuesta y enviar.
        Response<String> response = new Response<>(false,userMessage,null);
        return new ResponseEntity<>(response,HttpStatus.UNAUTHORIZED);
    }




    @ExceptionHandler(BlockAccountException.class)
    public ResponseEntity<Response> handleBlockAccountException(BlockAccountException ex) {

        // Cargar el mensaje de error para el log.
        String logMessege = messageService.getMessage(
                "exception.blockAccount.log",
                new Object[]{ex.getId(),ex.getUsername()},
                LocaleContextHolder.getLocale()
        );

        log.error(logMessege,ex);

        //Cargar el mensaje de error para el usuario.
        String userMessege = messageService.getMessage(
                "exception.blockAccount.user",
                null,
                LocaleContextHolder.getLocale()
        );

        //Construir respuesta y enviar.
        Response<String> response = new Response<>(false,userMessege,ex.getUsername());
        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Response> handleResourceNotFoundException(ResourceNotFoundException ex) {

        // Cargar el mensaje de error desde Base de datos
        String userMessage = messageService.getMessage(
                "exception.resourceNotFoundException.user", // La clave de la propiedad
                new Object[]{ex.getId()}, // Pasamos el username como parámetro
                LocaleContextHolder.getLocale() // Usamos el locale para la localización
        );

        // Crear la respuesta con el mensaje personalizado
        Response<String> response = new Response<>(false, userMessage, null);

        // Crear la respuesta con el mensaje personalizado
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);


    }

    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<Response<String>> handleUsernameNotFoundException(UserNameNotFoundException ex, HttpServletRequest request) {

        // Cargar el mensaje de error desde BD
        String logMessage = messageService.getMessage("exception.usernameNotFound.log", new Object[]{ex.getUsername()}, LocaleContextHolder.getLocale());
        log.error(logMessage);


        // Crear mensaje genérico para el usuario
        String userMessage = messageService.getMessage(
                "exception.usernameNotFound.user",
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
            String errorMessage = messageService.getMessage(error.getDefaultMessage(), null, LocaleContextHolder.getLocale());   // Guardamos el nombre del campo (error.getField()) y el mensaje de error correspondiente (error.getDefaultMessage()) en el mapa
            errors.put(error.getField(), errorMessage);

            //Se guarda Log
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (authentication != null) ? authentication.getName() : "Anónimo";

            String logMessage = messageService.getMessage("exception.validation.log",new Object[]{error.getField(),errorMessage,username}, LocaleContextHolder.getLocale());
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
        String userMessage = messageService.getMessage("exception.database.user", null, LocaleContextHolder.getLocale());

        //Se construye en el mensaje para log.
        String logMessage = messageService.getMessage(
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
        String messageUser = messageService.getMessage("exception.notFound", null, LocaleContextHolder.getLocale());
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
        String logMessage = messageService.getMessage("exception.accessDenied.log", new Object[]{username, requestedUrl, ex.getMessage()}, LocaleContextHolder.getLocale());

        // Loguear la excepción para detalles de diagnóstico
        log.error(logMessage, ex);

        // Mensaje para el usuario final
        String messageUser = messageService.getMessage("exception.accessDenied.user", null, LocaleContextHolder.getLocale());
        Response<String> response = new Response<>(false, messageUser, null);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }





    // Manejo de credenciales invalidad
    @ExceptionHandler({CredentialsException.class})
    public ResponseEntity<Response<String>> handleCredentialsException(CredentialsException ex, HttpServletRequest request) {
        //Obtener IP
        String ip = request.getRemoteAddr();

        // Cargar el mensaje de error para log.
        String logMessage = messageService.getMessage(
                "exception.badCredentials.log", // La clave de la propiedad
                new Object[]{ex.getUsername(),ip}, // Pasamos el username como parámetro
                LocaleContextHolder.getLocale() // Usamos el locale para la localización
        );

        log.error(logMessage);

        // Crear mensaje genérico para el usuario
        String userMessage = messageService.getMessage(
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

        //Construye mensaje para el Log.
        String logMessage = messageService.getMessage(
                "userService.save.passwordNotEquals.log",
                new Object[]{ ex.getUserNuevo(),userCreador},
                LocaleContextHolder.getLocale());

        //Log Error.
        log.error(logMessage);

        //Se construye mensaje para usuario.
        String userMessage = messageService.getMessage("userService.save.passwordNotEquals.user", null, LocaleContextHolder.getLocale());

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
        String messageUser = messageService.getMessage("exception.generic", null, LocaleContextHolder.getLocale());
        Response<String> response = new Response<>(false, messageUser, null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }





}
