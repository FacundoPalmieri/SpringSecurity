package com.securitysolution.spring.security.jwt.oauth2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Entidad que representa un permiso de usuario.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permissions")
public class Permission {

    /**Identificador único del permiso.*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /**Descripción del permiso.*/
    @Column( unique = true, nullable = false)
    private String permission;

    /**Nombre del permiso.*/
    @Column(unique = true, nullable = false)
    private String name;

}
