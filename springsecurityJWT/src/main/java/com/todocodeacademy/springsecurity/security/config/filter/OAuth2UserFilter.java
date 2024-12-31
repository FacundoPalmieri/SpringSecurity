package com.todocodeacademy.springsecurity.security.config.filter;

import com.todocodeacademy.springsecurity.exception.DataBaseException;
import com.todocodeacademy.springsecurity.model.UserSec;
import com.todocodeacademy.springsecurity.repository.IUserRepository;
import com.todocodeacademy.springsecurity.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Optional;

@Service
public class OAuth2UserFilter extends OncePerRequestFilter {

        @Autowired
        JwtUtils jwtUtils;

        @Autowired
        @Qualifier("messageSource")
        private MessageSource messageSource;

        private final IUserRepository userRepository;

        public OAuth2UserFilter(IUserRepository userRepository) {
            this.userRepository = userRepository;
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


                    // Verifica si el usuario está registrado en la base de datos.
                    Optional<UserSec> userOptional = userRepository.findUserEntityByUsername(email);
                    if (userOptional.isEmpty()) {
                        // Si el usuario no está registrado, se envía un error 403 (Forbidden) y se termina la solicitud.
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Usuario no registrado.");
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
                String userMessage = messageSource.getMessage(
                        "OAuth2.filter.error",
                        null,
                        LocaleContextHolder.getLocale());
                //Se guarda el motivo de la causa raíz
                String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

                throw new DataBaseException(userMessage,"OAuth2UserFilter",0L , "", "Método doFilterInternal", rootCause);

            }
        }
    }
