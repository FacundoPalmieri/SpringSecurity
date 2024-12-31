package com.todocodeacademy.springsecurity.service.interfaces;

public interface IEmailService {
    void sendEmail(String to, String subject, String body);
}
