package com.securitysolution.spring.security.jwt.oauth2.controller;

import com.securitysolution.spring.security.jwt.oauth2.dto.AuthLoginRequestDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.AuthResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.ResetPasswordDTO;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IUserService;
import com.securitysolution.spring.security.jwt.oauth2.service.UserDetailsServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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


    //Todas estas requests y responses vamos a tratarlas como dto
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthLoginRequestDTO userRequest) {
        return new ResponseEntity<>(this.userDetailsService.loginUser(userRequest), HttpStatus.OK);
    }

    @PostMapping("/request/reset-password")
    public ResponseEntity<String> requestResetPassword(@RequestParam String email) {
        return userService.createTokenResetPasswordForUser(email);

    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO, HttpServletRequest request) {
        return  userService.updatePassword(resetPasswordDTO, request);

    }



}





