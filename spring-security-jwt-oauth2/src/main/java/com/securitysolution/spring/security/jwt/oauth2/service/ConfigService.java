package com.securitysolution.spring.security.jwt.oauth2.service;


import com.securitysolution.spring.security.jwt.oauth2.dto.MessageDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.model.Message;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IConfigService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IFailedLoginAttemptsService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
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

    @Override
    public ResponseEntity<Response<List<Message>>>getMessage(){

        //Obtiene el listado.
        List<Message> listMessage = messageService.listMessage();

        //Se construye Mensaje para usuario.
        String userMessage = messageService.getMessage("config.getMessage.ok", null, LocaleContextHolder.getLocale());
        Response<List<Message>> response = new Response<>(true, userMessage,listMessage);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    @Transactional
    public ResponseEntity<Response<Message>> updateMessage(MessageDTO messageDto) {
        try{
            //Obtiene mensaje de BD
            Message message = messageService.getById(messageDto.id());

            //Actualiza campo
            message = messageService.updateMessage(message);

            //Prepara y envía respuesta.
            String userMessage = messageService.getMessage("config.updateMessage.ok",null,LocaleContextHolder.getLocale());

            Response<Message> response = new Response<>(true, userMessage,message);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "configRepository",messageDto.id(), "", "updateMessage");
        }

    }

    @Override
    public ResponseEntity<Response<Integer>> getAttempts() {

        //Obtiene el valor.
        Integer attempts = failedLoginAttemptsService.get();

        //Se construye Mensaje para usuario.
        String userMessage = messageService.getMessage("config.getAttempts.ok", null, LocaleContextHolder.getLocale());
        Response<Integer> response = new Response<>(true, userMessage,attempts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Response<Integer>> updateAttempts(Integer attempt) {

        //Actualiza valor
        failedLoginAttemptsService.update(attempt);

        //Recupera valor actualizado.
        Integer attempts = failedLoginAttemptsService.get();

        //Se construye Mensaje para usuario.
        String userMessage = messageService.getMessage("config.updateAttempts.ok", new Object[]{attempt}, LocaleContextHolder.getLocale());
        Response<Integer> response = new Response<>(true, userMessage,attempts);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
