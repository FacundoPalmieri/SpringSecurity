package com.securitysolution.spring.security.jwt.oauth2.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.securitysolution.spring.security.jwt.oauth2.dto.ResetPasswordDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecDTO;
import com.securitysolution.spring.security.jwt.oauth2.exception.*;
import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import com.securitysolution.spring.security.jwt.oauth2.repository.IUserRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IEmailService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IFailedLoginAttemptsService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IUserService;
import com.securitysolution.spring.security.jwt.oauth2.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
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
    private IMessageService messageService;

    @Autowired
    private IFailedLoginAttemptsService failedLoginAttemptsService;



    @Override
    public List<UserSec> findAll() {
        try{
            return userRepository.findAll();
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", 0L, "", "findAll");
        }
    }

    @Override
    public Optional<UserSec> findById(Long id) {
        try{
            return userRepository.findById(id);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", id, "", "findById");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<Response<UserSec>> save(UserSecDTO userSecDto) {

        //Valída que las pass sean coincidentes.
        validatePasswords(userSecDto.getPassword1(), userSecDto.getPassword2(),userSecDto.getUsername());

        //Construye objeto model
        UserSec userSec = buildUserSec(userSecDto);

        //Asigna Roles.
        userSec.setRolesList(getRolesForUser(userSecDto.getRolesList()));

        try{
            UserSec userSecOK = userRepository.save(userSec);

            //Se construye Mensaje para usuario.
            String userMessage = messageService.getMessage("userService.save.ok", null, LocaleContextHolder.getLocale());
            Response<UserSec> response = new Response<>(true, userMessage,userSecOK);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", userSec.getId(), userSec.getUsername(), "save");
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
    @Transactional
    public ResponseEntity<String> createTokenResetPasswordForUser(String email) {
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
            String dominio = messageService.getMessage("userService.dominio", null, LocaleContextHolder.getLocale());

            // Construir la URL de restablecimiento de contraseña
            String resetUrl = dominio + "?token=" + token;

            // Obtener el mensaje completo con la URL de restablecimiento
            String message = messageService.getMessage("userService.requestResetPassword.mensaje", new Object[] {resetUrl}, LocaleContextHolder.getLocale());

            //Asunto del email
            String asunto = messageService.getMessage("userService.requestResetPassword.asunto", null, LocaleContextHolder.getLocale());

            //Envío de email
            emailService.sendEmail(user.getUsername(), asunto, message);

            //Elaborar respuesta para el controller.
            String messageUser = messageService.getMessage("userService.requestResetPassword.success", null, LocaleContextHolder.getLocale());
            return ResponseEntity.ok(messageUser);


        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", 0L, "", "createPasswordReset");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> updatePassword(ResetPasswordDTO resetPasswordDTO, HttpServletRequest request) {
        try {
                //Valída Token de restablecimiento.
                validateTokenResetPassword(resetPasswordDTO.token());

                //Obtiene información del usuario.
                UserSec usuario = userRepository.findByResetPasswordToken(resetPasswordDTO.token());

                //Valída que coincidan las passwords.
                validatePasswords(resetPasswordDTO.newPassword1(), resetPasswordDTO.newPassword2(),usuario.getUsername());

                //Desbloquea la cuenta.
                unlockAccount(usuario);

                //Encripa la password
                String passwordEncrypted = encriptPassword(resetPasswordDTO.newPassword1());
                usuario.setPassword(passwordEncrypted);
                usuario.setResetPasswordToken(null);
                userRepository.save(usuario);

                //Envía correo al usuario.
                String message = messageService.getMessage("userService.resetPassword.success", null, LocaleContextHolder.getLocale());
                String asunto = messageService.getMessage("userService.resetPassword.asunto", null, LocaleContextHolder.getLocale());
                emailService.sendEmail(usuario.getUsername(), asunto, message);

                //Obtener dirección IP
                String ipAddress = request.getRemoteAddr();

                //Guardado de Log
                log.atInfo().log("[Mensaje: {}] - [USUARIO: {}] -[IP {}]", message,usuario.getUsername(), ipAddress);

                //Elabora Response a controller.
                String messageUser = messageService.getMessage("userService.resetPassword.success", null, LocaleContextHolder.getLocale());
                return ResponseEntity.ok(messageUser);

        } catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", 0L, "", "updatePassword");
        }
    }

    protected boolean verifyAttempts(String username){
        try{
            int configAttempts = failedLoginAttemptsService.get();
            int userAttempts = userRepository.findFailedLoginAttemptsByUsername(username);
            if(userAttempts >= configAttempts){
                return false;
            }
            return true;
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", 0L, username, "verifyAttempts");
        }
    }

    @Transactional
    protected UserSec blockAccount(String username){
        Long userId = 0L; // Variable para almacenar el ID del usuario
        try{
            Optional<UserSec>userSec = userRepository.findUserEntityByUsername(username);
            if(userSec.isEmpty()){
                throw new UserNameNotFoundException(username);
            }

            //Obtener id Usuario para exception
            userId = userSec.get().getId();

            UserSec user = userSec.get();
            user.setAccountNotLocked(false);
            user.setLocktime(LocalDateTime.now());
            user.setLastUpdateDateTime(LocalDateTime.now());
            return userRepository.save(user);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService",userId, username, "blockAccount");
        }
    }

    @Transactional
    protected void unlockAccount(UserSec user){
        try{
            user.setAccountNotLocked(true);
            user.setFailedLoginAttempts(0);
            user.setLocktime(null);
            user.setLastUpdateDateTime(LocalDateTime.now());
            userRepository.save(user);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService",user.getId(), user.getUsername(), "blockAccount");
        }
    }

    @Transactional
    protected void incrementFailedAttempts(String username) {
        Optional<UserSec> userSec = userRepository.findUserEntityByUsername(username);

        if(userSec.isPresent()) {
            UserSec userSecOK = userSec.get();
            userSecOK.setFailedLoginAttempts(userSecOK.getFailedLoginAttempts() + 1);
            userSecOK.setLastUpdateDateTime(LocalDateTime.now());
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
            userSecOK.setLastUpdateDateTime(LocalDateTime.now());
            try{
                userRepository.save(userSecOK);
            }catch (DataAccessException | CannotCreateTransactionException e) {
                throw new DataBaseException(e, "userService", userSecOK.getId(), username, "decrementFailedAttempts");
            }
        }
    }

    protected void enableAccount(String username){
        Long id = 0L;
        try{
            Optional<UserSec> userSec = userRepository.findUserEntityByUsername(username);
            if(userSec.isPresent()) {
                UserSec userSecOK = userSec.get();
                id = userSecOK.getId();
                if(!userSecOK.isEnabled()) {
                    throw new UserNameNotFoundException(username);
                }
            }
        }catch(DataAccessException | CannotCreateTransactionException e) {
                throw new DataBaseException(e, "userService", id, username, "enableAccount");
        }

    }

    private void validateTokenResetPassword(String token) {
        try {
            DecodedJWT decodedJWT = jwtUtils.validateToken(token);
            String username = jwtUtils.extractUsername(decodedJWT);
            UserSec usuario = userRepository.findByResetPasswordToken(token);
            if(usuario == null || !usuario.getUsername().equals(username)){
                throw new TokenInvalidException("",usuario.getUsername());
            }
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "userService", 0L, "", "validatePasswordReset");
        }
    }

    private void validatePasswords(String password1, String password2, String username) {
        if(!password1.equals(password2)){
            throw new PasswordMismatchException(username);
        }
    }

    private Set<Role> getRolesForUser(Set<Role> rolesList) {
        Set<Role> validRoles = new HashSet<>();
        for (Role role : rolesList) {
            Role foundRole = roleService.findById(role.getId()).orElseThrow(() ->
                    new RoleNotFoundException("",role.getId(), role.getRole())
            );
            validRoles.add(foundRole);
        }
        return validRoles;
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


}

