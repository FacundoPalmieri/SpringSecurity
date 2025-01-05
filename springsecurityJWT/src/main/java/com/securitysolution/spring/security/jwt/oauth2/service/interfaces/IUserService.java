package com.todocodeacademy.springsecurity.service.interfaces;

import com.todocodeacademy.springsecurity.dto.ResetPasswordDTO;
import com.todocodeacademy.springsecurity.model.UserSec;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {

     List<UserSec> findAll();
     Optional<UserSec> findById(Long id);
     UserSec save(UserSec userSec);
     void deleteById(Long id);
     void update(UserSec userSec);
     String encriptPassword(String password);
     ResponseEntity<String> createPasswordResetTokenForUser(String email);
     ResponseEntity<String> updatePassword(ResetPasswordDTO resetPasswordDTO, HttpServletRequest request);

}
