package com.todocodeacademy.springsecurity.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.todocodeacademy.springsecurity.exception.DataBaseException;
import com.todocodeacademy.springsecurity.model.UserSec;
import com.todocodeacademy.springsecurity.repository.IUserRepository;
import com.todocodeacademy.springsecurity.service.interfaces.IEmailService;
import com.todocodeacademy.springsecurity.service.interfaces.IUserService;
import com.todocodeacademy.springsecurity.utils.JwtUtils;
import jakarta.transaction.TransactionalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                    "user.findAll.error",
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
                    "user.findById.error",
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
            String userMessage = messageSource.getMessage("user.save.error", new Object[] {userSec.getUsername()}, LocaleContextHolder.getLocale());

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
                   "user.deleteById.error",
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
    public void createPasswordResetTokenForUser(String email) {
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

            // Enviar email con el token

            // Obtener la URL base desde el archivo properties.
            String dominio = messageSource.getMessage(
                    "userService.dominio",
                    null,
                    LocaleContextHolder.getLocale());

            // Construir la URL de restablecimiento de contraseña
            String resetUrl = dominio + "?token=" + token;

            // Obtener el mensaje completo con la URL de restablecimiento
            String message = messageSource.getMessage(
                    "userService.mensaje",
                    new Object[] {resetUrl},
                    LocaleContextHolder.getLocale());

            String asunto = messageSource.getMessage(
                    "userService.asunto",
                    null,
                    LocaleContextHolder.getLocale());

            emailService.sendEmail(user.getUsername(), asunto, message);


        }catch (DataAccessException | CannotCreateTransactionException e) {
            String userMessage = messageSource.getMessage(
                    "user.createPasswordReset.error",
                    new Object[] {email},
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "userService", 0L, "", "createPasswordReset", rootCause);
        }
    }

    public boolean validatePasswordResetToken(String token) {
        try {
            DecodedJWT decodedJWT = jwtUtils.validateToken(token);
            String username = jwtUtils.extractUsername(decodedJWT);
            UserSec usuario = userRepository.findByResetPasswordToken(token);
            return usuario != null && usuario.getUsername().equals(username);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            String userMessage = messageSource.getMessage(
                    "user.validatePasswordReset.error",
                    null,
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "userService", 0L, "", "validatePasswordReset", rootCause);
        }
    }

    public void updatePassword(String token, String newPassword) {
        try {

            UserSec usuario = userRepository.findByResetPasswordToken(token);
            String passwordEncrypted = encriptPassword(newPassword);
            if (usuario != null) {
                usuario.setPassword(passwordEncrypted);
                usuario.setResetPasswordToken(null);
                userRepository.save(usuario);
            }
        }catch (DataAccessException | CannotCreateTransactionException e) {
            String userMessage = messageSource.getMessage(
                    "user.updatePassword.error",
                    null,
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "userService", 0L, "", "updatePassword", rootCause);
        }
    }
}

