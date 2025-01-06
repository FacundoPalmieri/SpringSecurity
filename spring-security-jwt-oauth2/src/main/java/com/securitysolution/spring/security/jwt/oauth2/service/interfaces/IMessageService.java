package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.model.Message;

import java.util.List;
import java.util.Locale;

public interface IMessageService {
    String getMessage(String clave, Object[] objects, Locale locale);
    List<Message> listMessage();
    Message getById(Long id);
    Message updateMessage(Message message);


}
