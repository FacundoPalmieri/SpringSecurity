package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.exception.MessageNotFoundException;
import com.securitysolution.spring.security.jwt.oauth2.exception.ResourceNotFoundException;
import com.securitysolution.spring.security.jwt.oauth2.model.MessageConfig;
import com.securitysolution.spring.security.jwt.oauth2.repository.IMessageRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

@Service
public class MessageService implements IMessageService {

    @Autowired
    private IMessageRepository messageRepository;


    public String getMessage(String clave,Object[] args, Locale locale) {
        MessageConfig message = messageRepository.findByKeyAndLocale(clave, "es_AR");
        if (message != null) {
            return args == null ? message.getValue() : formatMessage(message.getValue(), args);        }
        return "Mensaje no encontrado";
    }


    private String formatMessage(String message,Object[] args) {
        return MessageFormat.format(message,args);
    }



    @Override
    public List<MessageConfig> listMessage() {
        try {
            return messageRepository.findAll();
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "configRepository", 0L, "", "findAll");
        }
    }

    @Override
    public MessageConfig getById(Long id) {
        try{
            return messageRepository.findById(id).orElseThrow(()->new MessageNotFoundException("", id, "MessageService", "getById"));

        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "configRepository", 0L, "", "findAll");
        }
    }

    @Override
    public MessageConfig updateMessage(MessageConfig message) {
        return messageRepository.save(message);
    }


}

