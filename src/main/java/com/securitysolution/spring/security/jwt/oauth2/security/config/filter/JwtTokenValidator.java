package com.securitysolution.spring.security.jwt.oauth2.security.config.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import com.securitysolution.spring.security.jwt.oauth2.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collection;

@Slf4j
//mediante el extends establecemos que es un filtro que se tiene que ejecutar siempre
public class JwtTokenValidator extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private IMessageService messageService;


    public JwtTokenValidator(JwtUtils jwtUtils, IMessageService messageService) {
        this.jwtUtils = jwtUtils;
        this.messageService = messageService;
    }

    @Override
    //importante: el nonnull debe ser de sringframework, no lombok
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (jwtToken != null) {


                //en el encabezado antes del token viene la palabra bearer (esquema de autenticación)
                //por lo que debemos sacarlo
                jwtToken = jwtToken.substring(7); //son 7 letras + 1 espacio
                DecodedJWT decodedJWT = jwtUtils.validateToken(jwtToken);

                //si el token es válido, le concedemos el acceso
                String username = jwtUtils.extractUsername(decodedJWT);
                //me devuelve claim, necesito pasarlo a String
                String authorities = jwtUtils.getSpecificClaim(decodedJWT, "authorities").asString();

                //Si todo está ok, hay que setearlo en el Context Holder
                //Para eso tengo que convertirlos a GrantedAuthority
                Collection<? extends GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                //Si se valida el token, le damos acceso al usuario en el context holder
                SecurityContext context = SecurityContextHolder.getContext();
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authoritiesList);
                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

            }

            // Continuar con el siguiente filtro
            filterChain.doFilter(request, response);
        }catch (TokenExpiredException ex){
            handleTokenExpiredException(ex, response);
        }catch (JWTVerificationException ex) {
            handleTokenInvalidException(ex,response);
        }
    }


    //Se realiza acá porque no va al manejador global el filtro.
    private void handleTokenExpiredException(TokenExpiredException ex, HttpServletResponse response) throws IOException {
        // Comprobar si hay una autenticación en el contexto
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "Anónimo";  // Valor por defecto si no se encuentra autenticación

        String logMessage = messageService.getMessage("exception.expiredToken.log", null, LocaleContextHolder.getLocale());
        log.error(logMessage, username);

        // Crear mensaje genérico para el usuario
        String userMessage = messageService.getMessage("exception.expiredToken.user", null, LocaleContextHolder.getLocale());

        // Capturamos la excepción y devolvemos una respuesta personalizada
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        // Crear la respuesta con el formato adecuado
        Response<String> customResponse = new Response<>(false, userMessage, null);

        // Convertir el objeto a JSON (usando una librería como Jackson)
        String jsonResponse = new ObjectMapper().writeValueAsString(customResponse);

        // Escribir la respuesta JSON en el cuerpo de la respuesta HTTP
        response.getWriter().write(jsonResponse);
    }

    private void handleTokenInvalidException(JWTVerificationException ex, HttpServletResponse response) throws IOException {

        // Comprobar si hay una autenticación en el contexto
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (authentication != null) ? authentication.getName() : "Unknown User";  // Default value if no authentication is found

        // Cargar el mensaje de error desde properties
        String logMessage = messageService.getMessage("exception.validateToken.log", new Object[]{username}, LocaleContextHolder.getLocale());
        log.error(logMessage, username,ex);

        // Crear mensaje genérico para el usuario
        String userMessage = messageService.getMessage("exception.validateToken.user", null, LocaleContextHolder.getLocale());

        // Capturamos la excepción y devolvemos una respuesta personalizada
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        // Crear la respuesta con el formato adecuado
        Response<String> customResponse = new Response<>(false, userMessage, null);

        // Convertir el objeto a JSON (usando una librería como Jackson)
        String jsonResponse = new ObjectMapper().writeValueAsString(customResponse);

        // Escribir la respuesta JSON en el cuerpo de la respuesta HTTP
        response.getWriter().write(jsonResponse);
    }
}


