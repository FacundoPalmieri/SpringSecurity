package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.exception.DataBaseException;
import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import com.securitysolution.spring.security.jwt.oauth2.repository.IRoleRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public List<Role> findAll() {
       try{
           return roleRepository.findAll();
       }catch(DataAccessException | CannotCreateTransactionException e){
           throw new DataBaseException(e,"roleService", 0L, "", "findAll");

       }
    }

    @Override
    public Optional<Role> findById(Long id) {

        try{
            return roleRepository.findById(id);

        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"roleService", id, "", "findById");

        }
    }

    @Override
    public Role save(Role role) {
        try{
            return roleRepository.save(role);
        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"roleService", role.getId(), role.getRole(), "save");

        }
    }

    @Override
    public void deleteById(Long id) {
        try{
            roleRepository.deleteById(id);
        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"roleService", id, "", "delete");

        }
    }

    @Override
    public Role update(Role role) {
        try {
            return roleRepository.save(role);
        } catch (DataAccessException | CannotCreateTransactionException e) {
            throw new DataBaseException(e, "roleService", role.getId(), role.getRole(), "update");


        }

    }
}
