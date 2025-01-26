package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.dto.FailedLoginAttemptsDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.MessageDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.TokenConfigDTO;
import com.securitysolution.spring.security.jwt.oauth2.model.MessageConfig;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface IConfigService {
    //Mensajes
    ResponseEntity<Response<List<MessageConfig>>> getMessage();
    ResponseEntity<Response<MessageConfig>> updateMessage(MessageDTO messageDto);


    //Intentos de inicio de sesión
    ResponseEntity<Response<Integer>> getAttempts();
    ResponseEntity<Response<Integer>> updateAttempts(FailedLoginAttemptsDTO failedLoginAttemptsDTO);

    //Expiración de Token.
    ResponseEntity<Response<Long>> getTokenExpiration();
    ResponseEntity<Response<Long>> updateTokenExpiration(TokenConfigDTO tokenConfigDTO);


}
