package com.todocodeacademy.springsecurity.service.interfaces;

import com.todocodeacademy.springsecurity.model.UserSec;

import java.util.List;
import java.util.Optional;

public interface IUserService {

     List<UserSec> findAll();
     Optional<UserSec> findById(Long id);
     UserSec save(UserSec userSec);
     void deleteById(Long id);
     void update(UserSec userSec);
     String encriptPassword(String password);
     void createPasswordResetTokenForUser(String email);
     boolean validatePasswordResetToken(String token);
     void updatePassword(String token, String newPassword);

}
