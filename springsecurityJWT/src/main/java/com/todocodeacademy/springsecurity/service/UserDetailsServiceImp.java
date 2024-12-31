package com.todocodeacademy.springsecurity.service;
import com.todocodeacademy.springsecurity.dto.AuthLoginRequestDTO;
import com.todocodeacademy.springsecurity.dto.AuthResponseDTO;
import com.todocodeacademy.springsecurity.exception.CredentialsException;
import com.todocodeacademy.springsecurity.model.UserSec;
import com.todocodeacademy.springsecurity.repository.IUserRepository;
import com.todocodeacademy.springsecurity.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private IUserRepository userRepo;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {

        //Contamos con usuario de tipo Usersec y necesitamos devolver un tipo UserDetails
        //Recuperamos el usuario de la bd
        UserSec userSec = userRepo.findUserEntityByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("El usuario " + username + "no fue encontrado"));

        //Spring Security maneja permisos con GrantedAuthority
        //Creamos una lista de SimpleGrantedAuthority para almacenar los permisos
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        //Programación funcional

        //Obtenemos roles y los convertimos en SimpleGrantedAuthority para poder agregarlos a la authorityList
        userSec.getRolesList()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));


        //Obtenemos los permisos y los agregamos a la lista.
        userSec.getRolesList().stream()
                .flatMap(role -> role.getPermissionsList().stream()) //acá recorro los permisos de los roles
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getPermissionName())));

        //Retornamos el usuario en formato Spring Security con los datos de nuestro userSec
        return new User(userSec.getUsername(),
                userSec.getPassword(),
                userSec.isEnabled(),
                userSec.isAccountNotExpired(),
                userSec.isCredentialNotExpired(),
                userSec.isAccountNotLocked(),
                authorityList);
    }

    public AuthResponseDTO loginUser (AuthLoginRequestDTO authLoginRequest){
        try {
            //recuperamos nombre de usuario y contraseña
            String username = authLoginRequest.username();
            String password = authLoginRequest.password();


            // Llamo al método authenticate.
            Authentication authentication = this.authenticate(username, password);

            //si es autenticado correctamente se almacena la información SecurityContextHolder y se crea el token.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtUtils.createToken(authentication);
            AuthResponseDTO authResponseDTO = new AuthResponseDTO(username, "login ok", accessToken, true);
            return authResponseDTO;
        }catch (BadCredentialsException ex) {

            // Cargar el mensaje de error desde properties
            String logMessage = messageSource.getMessage(
                    "exception.badCredentials.log", // La clave de la propiedad
                    new Object[]{authLoginRequest.username()}, // Pasamos el username como parámetro
                    LocaleContextHolder.getLocale() // Usamos el locale para la localización
            );

            // Crear mensaje genérico para el usuario
            String userMessage = messageSource.getMessage(
                    "exception.badCredentials.user",
                    null,
                    LocaleContextHolder.getLocale()
            );

            // Lanza la excepción con el mensaje genérico
            throw new CredentialsException(logMessage, userMessage);
        }



    }

    public Authentication authenticate (String username, String password) {
        //Recupero información del usuario por el username
        UserDetails userDetails = this.loadUserByUsername(username);

        // En caso que sea nulo, se informa que no se pudo encontrar al usuario.
        if (userDetails==null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        // En caso que no coincidan las credenciales se informa que la password es incorrecta
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

}
