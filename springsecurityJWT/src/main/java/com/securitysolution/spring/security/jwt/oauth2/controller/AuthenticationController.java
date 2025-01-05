package com.todocodeacademy.springsecurity.controller;

import com.todocodeacademy.springsecurity.dto.AuthLoginRequestDTO;
import com.todocodeacademy.springsecurity.dto.AuthResponseDTO;
import com.todocodeacademy.springsecurity.dto.ResetPasswordDTO;
import com.todocodeacademy.springsecurity.service.interfaces.IUserService;
import com.todocodeacademy.springsecurity.service.UserDetailsServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserDetailsServiceImp userDetailsService;

    @Autowired
    private IUserService userService;
    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    //Todas estas requests y responses vamos a tratarlas como dto
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO userRequest) {
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/request-reset-password")
    public ResponseEntity<String> requestResetPassword(@RequestParam String email) {
        return userService.createPasswordResetTokenForUser(email);

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, HttpServletRequest request) {

        return  userService.updatePassword(resetPasswordDTO, request);

    }

}





