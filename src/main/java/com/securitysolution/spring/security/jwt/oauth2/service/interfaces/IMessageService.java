package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.model.MessageConfig;

import java.util.List;
import java.util.Locale;

public interface IMessageService {
    String getMessage(String clave, Object[] objects, Locale locale);
    List<MessageConfig> listMessage();
    MessageConfig getById(Long id);
    MessageConfig updateMessage(MessageConfig message);


}
