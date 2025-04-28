package com.securitysolution.spring.security.jwt.oauth2.configuration.securityConfig.annotations;


import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole(T(com.securitysolution.spring.security.jwt.oauth2.enums.UserRole).Desarrollador.name())")
public @interface OnlyDevelopers {
}
