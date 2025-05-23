package com.securitysolution.spring.security.jwt.oauth2.exception;

import com.securitysolution.spring.security.jwt.oauth2.enums.LogLevel;
import org.springframework.http.HttpStatus;

/**
 * @author [Facundo Palmieri]
 */
public class BadRequestException extends AppException {
    public BadRequestException(String message, String userMessageKey, Object[] userArgs, String logMessageKey, Long id, String value, String clase, String method, LogLevel logLevel) {
        super(message, userMessageKey,userArgs,logMessageKey, id, value, clase, method, logLevel);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
