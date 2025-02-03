package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.dto.ResetPasswordDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Optional;

public interface IUserService {

     Response<List<UserSecResponseDTO>> findAll();
     Response<UserSecResponseDTO> findById(Long id);
     Response<UserSecResponseDTO> save(UserSecDTO userSecDto);
     String encriptPassword(String password);
     String createTokenResetPasswordForUser(String email);
     String updatePassword(ResetPasswordDTO resetPasswordDTO, HttpServletRequest request);


    void deleteById(Long id);
    // void update(UserSec userSec);

}
