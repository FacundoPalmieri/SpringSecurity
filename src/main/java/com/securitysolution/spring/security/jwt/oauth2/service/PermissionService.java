package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.dto.PermissionResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import com.securitysolution.spring.security.jwt.oauth2.repository.IPermissionRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermissionService {

    @Autowired
    private IPermissionRepository permissionRepository;

    @Autowired
    private IMessageService messageService;

    @Override
    public Response<List<PermissionResponseDTO>> findAll() {
        try{

            List<Permission> permissionList = permissionRepository.findAll();

            List<PermissionResponseDTO> permissionResponseDTOList = new ArrayList<>();
            for(Permission permission : permissionList) {
                permissionResponseDTOList.add(convertToDTO(permission));
            }

            String messageUser = messageService.getMessage("permissionService.findAll.ok", null, LocaleContextHolder.getLocale());
            return new Response<>(true, messageUser, permissionResponseDTOList);

        } catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e,"PermissionService", 0L,"","findAll");

        }
    }

    private PermissionResponseDTO convertToDTO(Permission permission) {
        PermissionResponseDTO permissionResponseDTO = new PermissionResponseDTO();
        permissionResponseDTO.setId(permission.getId());
        permissionResponseDTO.setPermission(permission.getPermissionName());
        return permissionResponseDTO;
    }

    @Override
    public Optional<Permission> findById(Long id) {
        try{
            return permissionRepository.findById(id);
        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"PermissionService", id, "","findById");

        }
    }

    @Override
    public Permission save(Permission permission) {
        try {
            return permissionRepository.save(permission);
        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"PermissionService", permission.getId(), permission.getPermissionName(), "save");

        }
    }
/*
    @Override
    public void deleteById(Long id) {
        try{
            permissionRepository.deleteById(id);
        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"PermissionService", id, "", "delete");
        }
    }

    @Override
    public Permission update(Permission permission) {
        try{
            return permissionRepository.save(permission);
        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"PermissionService", permission.getId(), permission.getPermissionName(), "update");
        }
    }

 */
}
