package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import com.securitysolution.spring.security.jwt.oauth2.repository.IPermissionRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService implements IPermissionService {

    @Autowired
    private IPermissionRepository permissionRepository;

    @Override
    public List<Permission> findAll() {
        try{
            return permissionRepository.findAll();
        } catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e,"PermissionService", 0L,"","findAll");

        }
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
}
