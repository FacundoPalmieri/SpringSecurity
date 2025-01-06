package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.dto.MessageDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.model.Message;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface IConfigService {

    ResponseEntity<Response<List<Message>>> getMessage();
    ResponseEntity<Response<Message>> updateMessage(MessageDTO messageDto);



    //Intentos de inicio de sesión
    ResponseEntity<Response<Integer>> getAttempts();
    ResponseEntity<Response<Integer>> updateAttempts(Integer attempt);




}
