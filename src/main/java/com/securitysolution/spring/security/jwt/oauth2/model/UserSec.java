package com.securitysolution.spring.security.jwt.oauth2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jdk.jfr.Name;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="users")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSec {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(max = 50, message = "El nombre de usuario no puede tener más de 50 caracteres.")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña no puede estar vacía.")
    private String password;

    @Column(name = "Intentos_Fallidos")
    private int failedLoginAttempts;

    @Column(name = "Fecha_Bloqueo")
    private LocalDateTime locktime;


    @Column(name= "Fecha_Creacion")
    private LocalDateTime creationDateTime;

    @Column(name = "ultima_Actualizacion")
    private LocalDateTime lastUpdateDateTime;

    @Column(name = "Activa",nullable = false, columnDefinition = "boolean default true")
    private boolean enabled;

    @Column(name = "No_Expirada", nullable = false, columnDefinition = "boolean default true")
    private boolean accountNotExpired;

    @Column(name = "No_Bloqueada",nullable = false, columnDefinition = "boolean default true")
    private boolean accountNotLocked;

    @Column(name = "Credenciales_NoExpiradas", nullable = false, columnDefinition = "boolean default true")
    private boolean credentialNotExpired;

    //Usamos Set porque no permite repetidos
    //List permite repetidos
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) //el eager carga todos los roles
    @JoinTable (name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns=@JoinColumn(name = "role_id"))
    private Set<Role> rolesList = new HashSet<>();

    @Column(name="Token_Rest",length = 500)
    private String resetPasswordToken;



}
