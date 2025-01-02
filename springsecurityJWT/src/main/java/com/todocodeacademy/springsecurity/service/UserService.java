package com.todocodeacademy.springsecurity.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.todocodeacademy.springsecurity.dto.ResetPasswordDTO;
import com.todocodeacademy.springsecurity.exception.DataBaseException;
import com.todocodeacademy.springsecurity.model.UserSec;
import com.todocodeacademy.springsecurity.repository.IUserRepository;
import com.todocodeacademy.springsecurity.service.interfaces.IEmailService;
import com.todocodeacademy.springsecurity.service.interfaces.IUserService;
import com.todocodeacademy.springsecurity.utils.JwtUtils;
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

import java.util.List;
import java.util.Optional;
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
    @Qualifier("messageSource")
    private MessageSource messageSource;



    @Override
    public List<UserSec> findAll() {
        try{
            return userRepository.findAll();
        }catch (DataBaseException e) {
            String userMessage = messageSource.getMessage(
                    "userService.findAll.error",
                    null,
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "userService", 0L, "", "findAll", rootCause);
        }
    }

    @Override
    public Optional<UserSec> findById(Long id) {
        try{
            return userRepository.findById(id);
        }catch (DataBaseException e) {
            String userMessage = messageSource.getMessage(
                    "userService.findById.error",
                    new Object[] { id },
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "userService", id, "", "findById", rootCause);
        }
    }

    @Override
    public UserSec save(UserSec userSec) {
        try{
            return userRepository.save(userSec);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            String userMessage = messageSource.getMessage("userService.save.error", new Object[] {userSec.getUsername()}, LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "userService", userSec.getId(), userSec.getUsername(), "save", rootCause);
        }
    }

    @Override
    public void deleteById(Long id) {

       try{
           userRepository.deleteById(id);
       }catch (DataAccessException | CannotCreateTransactionException e) {
           String userMessage = messageSource.getMessage(
                   "userService.deleteById.error",
                   new Object[] {id},
                   LocaleContextHolder.getLocale());

           //Se guarda el motivo de la causa raíz
           String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

           throw new DataBaseException(userMessage, "userService", id, "", "deleteById", rootCause);
       }
    }

    @Override
    public void update(UserSec userSec) {
        save(userSec);
    }

    @Override
    public String encriptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public ResponseEntity<String> createPasswordResetTokenForUser(String email) {
        try {
            Optional<UserSec> userOptional = userRepository.findUserEntityByUsername(email);
            if (userOptional.isEmpty()) {
                throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
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
            String userMessage = messageSource.getMessage(
                    "userService.createPasswordReset.error",
                    new Object[] {email},
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "userService", 0L, "", "createPasswordReset", rootCause);
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
            String userMessage = messageSource.getMessage(
                    "userService.updatePassword.error",
                    null,
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "userService", 0L, "", "updatePassword", rootCause);
        }
    }

    private boolean validatePasswordResetToken(String token) {
        try {
            DecodedJWT decodedJWT = jwtUtils.validateToken(token);
            String username = jwtUtils.extractUsername(decodedJWT);
            UserSec usuario = userRepository.findByResetPasswordToken(token);
            return usuario != null && usuario.getUsername().equals(username);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            String userMessage = messageSource.getMessage("userService.validatePasswordReset.error", null, LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "userService", 0L, "", "validatePasswordReset", rootCause);
        }
    }

}

