package com.securitysolution.spring.security.jwt.oauth2.repository;

import com.securitysolution.spring.security.jwt.oauth2.model.MessageConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMessageRepository extends JpaRepository<MessageConfig, Long> {

    MessageConfig findByKeyAndLocale(String clave, String locale);
}
