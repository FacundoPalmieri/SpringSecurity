package com.securitysolution.spring.security.jwt.oauth2.security.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.exception.BlockAccountException;
import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.exception.UserNameNotFoundException;
import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import com.securitysolution.spring.security.jwt.oauth2.repository.IUserRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.MessageService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import com.securitysolution.spring.security.jwt.oauth2.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;
@Slf4j
public class OAuth2UserFilter extends OncePerRequestFilter {

        private JwtUtils jwtUtils;
        private IMessageService messageService;
        private IUserRepository userRepository;

        public OAuth2UserFilter(JwtUtils jwtUtils,IUserRepository userRepository, IMessageService messageService) {
            this.jwtUtils = jwtUtils;
            this.userRepository = userRepository;
            this.messageService = messageService;
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response,
                                        FilterChain filterChain) throws ServletException, IOException, IOException {
            try{

                // Obtiene la autenticación actual del contexto de seguridad.
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                // Verifica si la autenticación no es nula y el principal es una instancia de DefaultOAuth2User.
                if (authentication != null && authentication.getPrincipal() instanceof DefaultOAuth2User) {
                    DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();

                    // Obtiene el email del usuario autenticado.
                    String email = oAuth2User.getAttribute("email");

                    // Recupera datos del usuario.
                    Optional<UserSec> userOptional = userRepository.findUserEntityByUsername(email);
                    UserSec user = userOptional.orElse(null);

                    //Verifica si existe en la BD o si la cuenta está inactiva.
                    if (user == null || !user.isEnabled()) {
                        // Si el usuario no existe o no está habilitado
                        handleEnableAccount(user, response);
                        return;
                    }

                    //Verifica si la cuenta está bloqueada
                    if (!user.isAccountNotLocked()) {
                        handleBlockAccount(user, response);
                        return;
                    }

                    // Si el usuario está registrado, se genera un JWT usando el método createToken.
                    String jwt = jwtUtils.createToken(authentication);

                    // Se incluye el token en el encabezado de la respuesta con el formato "Bearer [token]".
                     response.addHeader("Authorization", "Bearer " + jwt);
                }

                // Pasa la solicitud al siguiente filtro en la cadena de filtros.
                filterChain.doFilter(request, response);
            }catch (DataBaseException e){
                throw new DataBaseException(e,"OAuth2UserFilter",0L , "", "Método doFilterInternal");

            }

        }

        //Se realiza acá porque no va al manejador global el filtro.
        private boolean handleEnableAccount(UserSec user, HttpServletResponse response) throws IOException {
            if(user == null || !user.isEnabled()) {
                String logMessage = messageService.getMessage("exception.usernameNotFound.log", new Object[]{user != null ? user.getUsername() : "N/A"}, LocaleContextHolder.getLocale());
                log.error(logMessage);

                // Crear mensaje genérico para el usuario
                String messageUser = messageService.getMessage("exception.usernameNotFound.user", null, LocaleContextHolder.getLocale());

                // Capturamos la excepción y devolvemos una respuesta personalizada
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");

                // Crear la respuesta con el formato adecuado
                Response<String> customResponse = new Response<>(false, messageUser, null);

                // Convertir el objeto a JSON (usando una librería como Jackson)
                String jsonResponse = new ObjectMapper().writeValueAsString(customResponse);

                // Escribir la respuesta JSON en el cuerpo de la respuesta HTTP
                response.getWriter().write(jsonResponse);
                return true;
            }
            return false;

        }

    //Se realiza acá porque no va al manejador global el filtro.
    private boolean handleBlockAccount(UserSec user, HttpServletResponse response) throws IOException {

        if(!user.isAccountNotLocked()) {
            String logMessage = messageService.getMessage("exception.blockAccount.log",new Object[]{user.getId(),user.getUsername()},LocaleContextHolder.getLocale());
            log.error(logMessage);

            // Crear mensaje genérico para el usuario
            String messageUser = messageService.getMessage("exception.blockAccount.user", null, LocaleContextHolder.getLocale());

            // Capturamos la excepción y devolvemos una respuesta personalizada
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            // Crear la respuesta con el formato adecuado
            Response<String> customResponse = new Response<>(false, messageUser, null);

            // Convertir el objeto a JSON (usando una librería como Jackson)
            String jsonResponse = new ObjectMapper().writeValueAsString(customResponse);

            // Escribir la respuesta JSON en el cuerpo de la respuesta HTTP
            response.getWriter().write(jsonResponse);
            return true;
        }
        return false;

    }
}
