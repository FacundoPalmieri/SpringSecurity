package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.exception.ResourceNotFoundException;
import com.securitysolution.spring.security.jwt.oauth2.model.Message;
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
        Message message = messageRepository.findByKeyAndLocale(clave, locale.toString());
        if (message != null) {
            return args == null ? message.getValue() : formatMessage(message.getValue(), args);        }
        return "Mensaje no encontrado";
    }


    private String formatMessage(String message,Object[] args) {
        return MessageFormat.format(message,args);
    }



    @Override
    public List<Message> listMessage() {
        try {
            return messageRepository.findAll();
        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "configRepository", 0L, "", "findAll");
        }
    }

    @Override
    public Message getById(Long id) {
        try{
            return messageRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("",id));

        }catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "configRepository", 0L, "", "findAll");
        }
    }

    @Override
    public Message updateMessage(Message message) {
        return messageRepository.save(message);
    }


}
