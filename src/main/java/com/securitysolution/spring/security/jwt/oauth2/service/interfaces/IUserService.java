package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.dto.ResetPasswordDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecDTO;
import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {

     List<UserSec> findAll();
     Optional<UserSec> findById(Long id);
     ResponseEntity<Response<UserSec>> save(UserSecDTO userSecDto);
     String encriptPassword(String password);
     ResponseEntity<String> createTokenResetPasswordForUser(String email);
     ResponseEntity<String> updatePassword(ResetPasswordDTO resetPasswordDTO, HttpServletRequest request);


    void deleteById(Long id);
    // void update(UserSec userSec);

}
