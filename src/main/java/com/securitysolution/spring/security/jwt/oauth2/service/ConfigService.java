package com.securitysolution.spring.security.jwt.oauth2.service;


import com.securitysolution.spring.security.jwt.oauth2.dto.FailedLoginAttemptsDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.MessageDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.TokenConfigDTO;
import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.exception.MessageNotFoundException;
import com.securitysolution.spring.security.jwt.oauth2.model.MessageConfig;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IConfigService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IFailedLoginAttemptsService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.ITokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConfigService implements IConfigService {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IFailedLoginAttemptsService failedLoginAttemptsService;

    @Autowired
    private ITokenService tokenService;

    @Override
    public Response<List<MessageConfig>>getMessage(){

        //Obtiene el listado.
        List<MessageConfig> listMessage = messageService.listMessage();

        //Se construye Mensaje para usuario.
        String userMessage = messageService.getMessage("config.getMessage.ok", null, LocaleContextHolder.getLocale());
        return new Response<>(true, userMessage, listMessage);
    }

    @Override
    @Transactional
    public Response<MessageConfig> updateMessage(MessageDTO messageDto) {
        try{
            // valída y Obtiene mensaje de BD
            MessageConfig message = messageService.getById(messageDto.id());

            //Actualiza campo
            message = messageService.updateMessage(message);

            //Prepara y envía respuesta.
            String userMessage = messageService.getMessage("config.updateMessage.ok",null,LocaleContextHolder.getLocale());

            return new Response<>(true, userMessage,message);

        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "configRepository",messageDto.id(), "", "updateMessage");
        }

    }


    @Override
    public Response<Integer> getAttempts() {

        //Obtiene el valor.
        Integer attempts = failedLoginAttemptsService.get();

        //Se construye Mensaje para usuario.
        String userMessage = messageService.getMessage("config.getAttempts.ok", null, LocaleContextHolder.getLocale());
        return new Response<>(true, userMessage,attempts);

    }

    @Override
    public Response<Integer> updateAttempts(FailedLoginAttemptsDTO failedLoginAttemptsDTO) {

        //Actualiza valor
        failedLoginAttemptsService.update(failedLoginAttemptsDTO.value());

        //Recupera valor actualizado.
        Integer attempts = failedLoginAttemptsService.get();

        //Se construye Mensaje para usuario.
        String userMessage = messageService.getMessage("config.updateAttempts.ok", new Object[]{attempts}, LocaleContextHolder.getLocale());
        return new Response<>(true, userMessage,attempts);

    }

    @Override
    public Response<Long> getTokenExpiration() {

        // Obtener el valor en milisegundos
        Long expiration = tokenService.getExpiration();

        //Convertir a minutos
        Long expirationMinutes = (expiration / 1000) / 60;

        //Se construye Mensaje para usuario.
        String userMessage = messageService.getMessage("config.getExpirationToken.ok", null, LocaleContextHolder.getLocale());
        return new Response<>(true, userMessage,expirationMinutes);

    }

    @Override
    public Response<Long> updateTokenExpiration(TokenConfigDTO tokenConfigDTO) {

        //Convertir minutos a milisegundos
        Long milliseconds = (tokenConfigDTO.expiration() * 60) * 1000;

        //Actualizar tiempo de expiración.
        tokenService.updateExpiration(milliseconds);

        //Recuperar valor actualizado y convertirlo a minutos
        Long expiration = tokenService.getExpiration();
        Long expirationMinutes = (expiration / 1000) / 60;

        //Se construye Mensaje para usuario.
        String userMessage = messageService.getMessage("config.updateExpirationToken.ok", new Object[]{expirationMinutes}, LocaleContextHolder.getLocale());
        return new Response<>(true, userMessage,expirationMinutes);
    }


}
