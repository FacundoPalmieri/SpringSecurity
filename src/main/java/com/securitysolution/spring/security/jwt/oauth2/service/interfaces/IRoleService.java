package com.securitysolution.spring.security.jwt.oauth2.service.interfaces;

import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.RoleDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.RoleResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.model.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    Response<List<Role>> findAll();
    Response<RoleResponseDTO> getById(Long id);
    Optional<Role> findById(Long id);
    Response<RoleResponseDTO> save(RoleDTO roleDto);
    void deleteById(Long id);

}
