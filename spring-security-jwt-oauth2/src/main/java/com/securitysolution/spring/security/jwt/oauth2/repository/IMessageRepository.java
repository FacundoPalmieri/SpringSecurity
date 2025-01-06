package com.securitysolution.spring.security.jwt.oauth2.repository;

import com.securitysolution.spring.security.jwt.oauth2.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IMessageRepository extends JpaRepository<Message, Long> {

    Message findByKeyAndLocale(String clave, String locale);
}
