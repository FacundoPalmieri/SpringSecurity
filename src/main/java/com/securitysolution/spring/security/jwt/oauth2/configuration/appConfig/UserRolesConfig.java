package com.securitysolution.spring.security.jwt.oauth2.configuration.appConfig;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class UserRolesConfig {

    @Value("${custom.roles.user}")
    private String userRole;

    @Value("${custom.roles.admin}")
    private String adminRole;

    @Value("${custom.roles.dev}")
    private String devRole;

}
