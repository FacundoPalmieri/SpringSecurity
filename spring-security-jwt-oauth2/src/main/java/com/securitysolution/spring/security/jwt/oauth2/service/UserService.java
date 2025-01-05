package com.securitysolution.spring.security.jwt.oauth2.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.securitysolution.spring.security.jwt.oauth2.dto.ResetPasswordDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecDTO;
import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.exception.PasswordMismatchException;
import com.securitysolution.spring.security.jwt.oauth2.exception.UserNameNotFoundException;
import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import com.securitysolution.spring.security.jwt.oauth2.repository.IUserRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IEmailService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IUserService;
import com.securitysolution.spring.security.jwt.oauth2.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IEmailService emailService;

    @Autowired
    private RoleService roleService;

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;



    @Override
    public List<UserSec> findAll() {
        try{
            return userRepository.findAll();
        }catch (DataBaseException e) {
            throw new DataBaseException(e, "userService", 0L, "", "findAll");
        }
    }

    @Override
    public Optional<UserSec> findById(Long id) {
        try{
            return userRepository.findById(id);
        }catch (DataBaseException e) {
            throw new DataBaseException(e, "userService", id, "", "findById");
        }
    }

    @Override
    public ResponseEntity<Response<UserSec>> save(UserSecDTO userSecDto) {

        //Valída que las pass sean coincidentes.
        validatePasswords(userSecDto);

        //Construye objeto model
        UserSec userSec = buildUserSec(userSecDto);

        //Asigna Roles.
        userSec.setRolesList(getRolesForUser(userSecDto.getRolesList()));
        try{
            UserSec userSecOK = userRepository.save(userSec);

            //Se construye Mensaje para usuario.
            String userMessage = messageSource.getMessage("userService.save.ok", null, LocaleContextHolder.getLocale());
            Response<UserSec> response = new Response<>(true, userMessage,userSecOK);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", userSec.getId(), userSec.getUsername(), "save");
        }
    }

    private void validatePasswords(UserSecDTO userSecDto) {
        if(!userSecDto.getPassword1().equals(userSecDto.getPassword2())) {
            throw new PasswordMismatchException(userSecDto.getUsername());
        }
    }

    private Set<Role> getRolesForUser(Set<Role> rolesList){
        return rolesList.stream()
                .map(role -> roleService.findById(role.getId()).orElse(null))
                .collect(Collectors.toSet());
    }

    private UserSec buildUserSec(UserSecDTO userSecDto) {
        return UserSec.builder()
                .username(userSecDto.getUsername())
                .password(encriptPassword(userSecDto.getPassword1()))
                .failedLoginAttempts(0)
                .locktime(null)
                .creationDateTime(LocalDateTime.now())
                .lastUpdateDateTime(LocalDateTime.now())
                .enabled(true)
                .accountNotExpired(true)
                .accountNotLocked(true)
                .credentialNotExpired(true)
                .build();
    }

    @Transactional
    protected void incrementFailedAttempts(String username) {
        Optional<UserSec> userSec = userRepository.findUserEntityByUsername(username);

        if(userSec.isPresent()) {
            UserSec userSecOK = userSec.get();
            userSecOK.setFailedLoginAttempts(userSecOK.getFailedLoginAttempts() + 1);
            try {
                userRepository.save(userSecOK);
            }catch (DataAccessException | CannotCreateTransactionException e) {
                throw new DataBaseException(e, "userService", userSecOK.getId(), username, "incrementFailedAttempts");
            }
        }
    }

    @Transactional
    protected void decrementFailedAttempts(String username) {
        Optional<UserSec> userSec = userRepository.findUserEntityByUsername(username);
        if(userSec.isPresent()) {
            UserSec userSecOK = userSec.get();
            userSecOK.setFailedLoginAttempts(0);
            System.out.println("INTENTOS 0");
            try{
                userRepository.save(userSecOK);
            }catch (DataAccessException | CannotCreateTransactionException e) {
                throw new DataBaseException(e, "userService", userSecOK.getId(), username, "decrementFailedAttempts");
            }
        }
    }

    @Override
    public void deleteById(Long id) {

       try{
           userRepository.deleteById(id);
       }catch (DataAccessException | CannotCreateTransactionException e) {
           throw new DataBaseException(e, "userService", id, "", "deleteById");
       }
    }

    /*
    @Override
    public void update(UserSec userSec) {
        save(userSec);
    }


     */
    @Override
    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public ResponseEntity<String> createPasswordResetTokenForUser(String email) {
        try {
            Optional<UserSec> userOptional = userRepository.findUserEntityByUsername(email);
            if (userOptional.isEmpty()) {
                throw new UserNameNotFoundException(email);
            }

            UserSec user = userOptional.get();

            // Crear los authorities manualmente desde el usuario
            List<GrantedAuthority> authorities = user.getRolesList().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getRole()))
                    .collect(Collectors.toList());


            // Crear autenticación usando el username y los authorities
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);

            // Generar el token
            String token = jwtUtils.createToken(authentication);

            // Asignar el token al usuario
            user.setResetPasswordToken(token);
            userRepository.save(user);

            // Obtener la URL base desde el archivo properties.
            String dominio = messageSource.getMessage("userService.dominio", null, LocaleContextHolder.getLocale());

            // Construir la URL de restablecimiento de contraseña
            String resetUrl = dominio + "?token=" + token;

            // Obtener el mensaje completo con la URL de restablecimiento
            String message = messageSource.getMessage("userService.requestResetPassword.mensaje", new Object[] {resetUrl}, LocaleContextHolder.getLocale());

            //Asunto del email
            String asunto = messageSource.getMessage("userService.requestResetPassword.asunto", null, LocaleContextHolder.getLocale());

            //Envío de email
            emailService.sendEmail(user.getUsername(), asunto, message);

            //Elaborar respuesta para el controller.
            String messageUser = messageSource.getMessage("userService.requestResetPassword.success", null, LocaleContextHolder.getLocale());
            return ResponseEntity.ok(messageUser);


        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", 0L, "", "createPasswordReset");
        }
    }

    public ResponseEntity<String> updatePassword(ResetPasswordDTO resetPasswordDTO, HttpServletRequest request) {
        try {
            //Valída Token de restablecimiento.
            boolean isTokenValid = validatePasswordResetToken(resetPasswordDTO.token());
            if (!isTokenValid) {
                String messageUser = messageSource.getMessage("userService.resetPassword.error", null, LocaleContextHolder.getLocale());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageUser);
            }

            UserSec usuario = userRepository.findByResetPasswordToken(resetPasswordDTO.token());
            String passwordEncrypted = encriptPassword(resetPasswordDTO.newPassword());
            if (usuario != null) {
                usuario.setPassword(passwordEncrypted);
                usuario.setResetPasswordToken(null);
                userRepository.save(usuario);

                //Envía correo al usuario.
                String message = messageSource.getMessage("userService.resetPassword.success", null, LocaleContextHolder.getLocale());
                String asunto = messageSource.getMessage("userService.resetPassword.asunto", null, LocaleContextHolder.getLocale());
                emailService.sendEmail(usuario.getUsername(), asunto, message);

                //Obtener dirección IP
                String ipAddress = request.getRemoteAddr();

                //Guardado de Log
                log.atInfo().log("[Mensaje: {}] - [USUARIO: {}] -[IP {}]", message,usuario.getUsername(), ipAddress);

                //Elabora Response a controller.
                String messageUser = messageSource.getMessage("userService.resetPassword.success", null, LocaleContextHolder.getLocale());
                return ResponseEntity.ok(messageUser);
            }
            String messageUser = messageSource.getMessage("userService.resetPassword.error", null, LocaleContextHolder.getLocale());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageUser);

        } catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", 0L, "", "updatePassword");
        }
    }

    private boolean validatePasswordResetToken(String token) {
        try {
            DecodedJWT decodedJWT = jwtUtils.validateToken(token);
            String username = jwtUtils.extractUsername(decodedJWT);
            UserSec usuario = userRepository.findByResetPasswordToken(token);
            return usuario != null && usuario.getUsername().equals(username);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", 0L, "", "validatePasswordReset");
        }
    }

}

