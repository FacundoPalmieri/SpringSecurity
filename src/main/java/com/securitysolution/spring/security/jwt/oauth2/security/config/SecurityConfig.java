package com.securitysolution.spring.security.jwt.oauth2.security.config;
import com.securitysolution.spring.security.jwt.oauth2.repository.IUserRepository;
import com.securitysolution.spring.security.jwt.oauth2.security.config.filter.JwtTokenValidator;
import com.securitysolution.spring.security.jwt.oauth2.security.config.filter.OAuth2UserFilter;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import com.securitysolution.spring.security.jwt.oauth2.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IMessageService messageService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .defaultSuccessUrl("/holaseg",true)) //Redirección luego de autenticación.
                //sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtTokenValidator(jwtUtils, messageService), BasicAuthenticationFilter.class)
                .addFilterBefore(new OAuth2UserFilter(jwtUtils,userRepository,messageService), BasicAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2
                      .defaultSuccessUrl("/holaseg",true))//Redirección luego de autenticación.
                .build();
    }


    //creamos authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    //creamos authentication provider
    //Agregamos el user Details Service como parámetro
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());

        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

    //password encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
         return new BCryptPasswordEncoder();
    }


}