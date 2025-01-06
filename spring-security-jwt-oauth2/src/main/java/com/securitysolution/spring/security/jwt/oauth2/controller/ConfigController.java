package com.securitysolution.spring.security.jwt.oauth2.controller;

import com.securitysolution.spring.security.jwt.oauth2.dto.MessageDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.model.Message;
import com.securitysolution.spring.security.jwt.oauth2.service.ConfigService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dev")
@PreAuthorize("hasRole('DEV')")
public class ConfigController {
    @Autowired
    private ConfigService configService;

    @GetMapping("/message/get")
    public ResponseEntity<Response<List<Message>>> getMessage() {
        return configService.getMessage();
    }

    @PatchMapping("/message/update")
    public ResponseEntity<Response<Message>> updateMessage(@Valid @RequestBody MessageDTO messageDTO) {
        return configService.updateMessage(messageDTO);
    }

    // Intentos fallidos de sesión
    @GetMapping("/session/get")
    public ResponseEntity<Response<Integer>> getAttempts() {
        return configService.getAttempts();
    }


    @PatchMapping("/session/update")
    public ResponseEntity<Response<Integer>> updateAttempts(@NotNull @RequestParam("attempts") Integer attempts) {
        return configService.updateAttempts(attempts);
    }

}
