package com.securitysolution.spring.security.jwt.oauth2.service;
import com.securitysolution.spring.security.jwt.oauth2.dto.AuthLoginRequestDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.AuthResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.exception.BlockAccountException;
import com.securitysolution.spring.security.jwt.oauth2.exception.CredentialsException;
import com.securitysolution.spring.security.jwt.oauth2.exception.UserNameNotFoundException;
import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import com.securitysolution.spring.security.jwt.oauth2.repository.IUserRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import com.securitysolution.spring.security.jwt.oauth2.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private IMessageService messageService;

    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {


        //Contamos con usuario de tipo Usersec y necesitamos devolver un tipo UserDetails
        //Recuperamos el usuario de la bd
        UserSec userSec = userRepo.findUserEntityByUsername(username)
                .orElseThrow(()-> new UserNameNotFoundException(username));

        //Spring Security maneja permisos con GrantedAuthority
        //Creamos una lista de SimpleGrantedAuthority para almacenar los permisos
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();


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

            // Se invoca al método authenticate.
            Authentication authentication = this.authenticate(username, password);

            //si es autenticado correctamente se almacena la información SecurityContextHolder y se crea el token.
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtUtils.createToken(authentication);
            AuthResponseDTO authResponseDTO = new AuthResponseDTO(username, "Login OK", accessToken, true);
            return authResponseDTO;
        }catch (BadCredentialsException ex) {
            throw new CredentialsException(authLoginRequest.username());
        }
    }


    public Authentication authenticate (String username, String password) {
        //Recupero información del usuario por el username
        UserDetails userDetails = this.loadUserByUsername(username);

        // En caso que sea nulo, se informa que no se pudo encontrar al usuario.
        if (userDetails == null) {
            String logMessage = messageService.getMessage("exception.UsernameNotFound.log", new Object[]{username}, LocaleContextHolder.getLocale());
            throw new UserNameNotFoundException(username);
        }

        //En caso que no coincidan las credenciales se informa que la password es incorrecta
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {

            //Se incrementa intentos fallidos.
            userService.incrementFailedAttempts(username);

            //Verifica intentos de inicio de sesión.
            boolean status = userService.verifyAttempts(username);

            //Se bloquea en caso de igualar o exceder el limite.
            if(!status){
                UserSec userSec = userService.blockAccount(username);
                throw new BlockAccountException("",userSec.getId(), userSec.getUsername());
            }
            throw new CredentialsException(username);
        }

        //Verifica si está activa la cuenta.
        userService.enableAccount(username);

        //Resetea intentos fallidos a 0.
        userService.decrementFailedAttempts(username);
        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }



}
