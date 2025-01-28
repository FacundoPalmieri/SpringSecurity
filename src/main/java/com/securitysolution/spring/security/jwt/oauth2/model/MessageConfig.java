package com.securitysolution.spring.security.jwt.oauth2.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Mensajes")
public class MessageConfig {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="clave", unique = true, nullable = false)
    private String key;

    @Column(name = "valor")
    private  String value;

    @Column(name="locale")
    private String locale;

}
