package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.dto.PermissionResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.model.Permission;

import java.util.List;
import java.util.Optional;

public interface IPermissionService {

    Response<List<PermissionResponseDTO>> findAll();
    Response<PermissionResponseDTO> getById(Long id);
    Optional<Permission> findById(Long id);
 // Permission save(Permission permission);
 // void deleteById(Long id);
 // Permission update(Permission permission);

}
