package com.todocodeacademy.springsecurity.service;

import com.todocodeacademy.springsecurity.exception.DataBaseException;
import com.todocodeacademy.springsecurity.model.Role;
import com.todocodeacademy.springsecurity.repository.IRoleRepository;
import com.todocodeacademy.springsecurity.service.interfaces.IRoleService;
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
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messageSource;

    @Override
    public List<Role> findAll() {
       try{
           return roleRepository.findAll();
       }catch(DataAccessException | CannotCreateTransactionException e){
           String userMessage = messageSource.getMessage(
                   "role.findAll.error",
                   null,
                   LocaleContextHolder.getLocale());

           //Se guarda el motivo de la causa raíz
           String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

           throw new DataBaseException(userMessage,"roleService", 0L, "", "findAll", rootCause);

       }
    }

    @Override
    public Optional<Role> findById(Long id) {

        try{
            return roleRepository.findById(id);

        }catch(DataAccessException | CannotCreateTransactionException e){
            String userMessage = messageSource.getMessage(
                    "role.findById.error",
                    new Object[] { id },
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage,"roleService", id, "", "findById", rootCause);

        }
    }

    @Override
    public Role save(Role role) {
        try{
            return roleRepository.save(role);
        }catch(DataAccessException | CannotCreateTransactionException e){
            String userMessage = messageSource.getMessage(
                    "role.save.error",
                    new Object[] {role.getRole()},
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage,"roleService", role.getId(), role.getRole(), "save", rootCause);

        }
    }

    @Override
    public void deleteById(Long id) {
        try{
            roleRepository.deleteById(id);
        }catch(DataAccessException | CannotCreateTransactionException e){
            String userMessage = messageSource.getMessage(
                    "role.deleteById.error",
                    new Object[] {id},
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage,"roleService", id, "", "delete", rootCause);

        }
    }

    @Override
    public Role update(Role role) {
        try {
            return roleRepository.save(role);
        } catch (DataAccessException | CannotCreateTransactionException e) {
            String userMessage = messageSource.getMessage(
                    "role.update.error",
                    new Object[]{role.getRole()},
                    LocaleContextHolder.getLocale());

            //Se guarda el motivo de la causa raíz
            String rootCause = e.getCause() != null ? e.getCause().toString() : "Desconocida";

            throw new DataBaseException(userMessage, "roleService", role.getId(), role.getRole(), "update", rootCause);


        }

    }
}
