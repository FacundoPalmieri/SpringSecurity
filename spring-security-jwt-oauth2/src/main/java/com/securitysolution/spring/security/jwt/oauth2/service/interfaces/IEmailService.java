package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

public interface IEmailService {
    void sendEmail(String to, String subject, String body);
}
