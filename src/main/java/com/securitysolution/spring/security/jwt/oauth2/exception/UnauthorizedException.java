package com.securitysolution.spring.security.jwt.oauth2.exception;

import org.springframework.http.HttpStatus;

/**
 * @author [Facundo Palmieri]
 */
public class UnauthorizedException extends AppException {
    public UnauthorizedException(String message, String userMessageKey,Object[] userArgs,String logMessageKey, Long id, String value, String clase, String method,LogLevel logLevel) {
        super(message, userMessageKey,userArgs,logMessageKey, id, value, clase, method, logLevel);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
