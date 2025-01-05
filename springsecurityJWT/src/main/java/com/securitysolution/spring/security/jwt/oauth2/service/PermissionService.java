package com.todocodeacademy.springsecurity.service;

import com.todocodeacademy.springsecurity.exception.DataBaseException;
import com.todocodeacademy.springsecurity.model.Permission;
import com.todocodeacademy.springsecurity.repository.IPermissionRepository;
import com.todocodeacademy.springsecurity.service.interfaces.IPermissionService;
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


    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<Permission> findAll() {
        try{
            return permissionRepository.findAll();
        }catch(DataAccessException | CannotCreateTransactionException e){
            String userMessage = messageSource.getMessage(
                    "permission.findAll.error",
                    null,
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage,"Permission", 0L, "", "findAll", rootCause);

        }
    }

    @Override
    public Optional<Permission> findById(Long id) {
        try{
            return permissionRepository.findById(id);
        }catch(DataAccessException | CannotCreateTransactionException e){
            String userMessage = messageSource.getMessage(
                    "permission.findById.error",
                    new Object[]{id},
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage,"Permission", 0L, "", "findById", rootCause);

        }
    }

    @Override
    public Permission save(Permission permission) {
        try {
            return permissionRepository.save(permission);
        }catch(DataAccessException | CannotCreateTransactionException e){
            String userMessage = messageSource.getMessage(
                    "permission.save.error",
                    new Object[]{permission.getPermissionName()},
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage,"PermissionService", permission.getId(), permission.getPermissionName(), "save", rootCause);

        }
    }

    @Override
    public void deleteById(Long id) {
        try{
            permissionRepository.deleteById(id);
        }catch(DataAccessException | CannotCreateTransactionException e){
            String userMessage = messageSource.getMessage(
                    "permission.delete.error",
                    new Object[]{id},
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage,"PermissionService", id, "", "delete", rootCause);

        }
    }

    @Override
    public Permission update(Permission permission) {
        try{
            return permissionRepository.save(permission);
        }catch(DataAccessException | CannotCreateTransactionException e){
            String userMessage = messageSource.getMessage(
                    "permission.update.error",
                    new Object[]{permission.getPermissionName()},
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage,"PermissionService", permission.getId(), permission.getPermissionName(), "update", rootCause);

        }
    }
}
