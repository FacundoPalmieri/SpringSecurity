package com.securitysolution.spring.security.jwt.oauth2.service;

import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.RoleDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.RoleResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.exception.*;
import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import com.securitysolution.spring.security.jwt.oauth2.repository.IRoleRepository;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IMessageService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IPermissionService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private IMessageService messageService;

    @Override
    public Response<List<Role>> findAll() {
       try{
           List<Role> roleList =  roleRepository.findAll();

           String messageUser = messageService.getMessage("roleDTO.findAll.user.ok", null, LocaleContextHolder.getLocale());
           return new Response<>(true, messageUser, roleList);

       }catch(DataAccessException | CannotCreateTransactionException e){
           throw new DataBaseException(e,"roleService", 0L, "", "findAll");

       }
    }

    //Metodo consumido por la api
    @Override
    public Response<RoleResponseDTO> getById(Long id) {
        try{
              Optional<Role> role = roleRepository.findById(id);
              if(role.isPresent()){
                  RoleResponseDTO dto = convertToDTOResponse(role.get());

                  String messageUser = messageService.getMessage("roleDTO.findById.user.ok", null, LocaleContextHolder.getLocale());
                  return new Response<>(true, messageUser, dto);
              }else{
                  throw new RoleNotFoundException("",id, "","RoleService", "getById");
              }

        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"roleService", id, "", "findById");

        }
    }


    //Metodo consumido por otros servicios
    @Override
    public Optional<Role> findById(Long id) {

        try{
            return roleRepository.findById(id);

        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"roleService", id, "", "findById");

        }
    }

    @Override
    public Response<RoleResponseDTO> save(RoleDTO roleDto) {

        //Valída que el rol no exista en la base de datos.
        validateRol(roleDto.getRole());

        //Asigna permisos al rol
        roleDto.setPermissionsList(getPermissionForRole(roleDto.getPermissionsList()));

        //Se construye el Objeto model
        Role role = buildRole(roleDto);

        try{
            //Guarda el objeto en la base de datos.
            Role savedRole = roleRepository.save(role);

            //Conviente la entidad a un DTO
            RoleResponseDTO savedRoleDto = convertToDTOResponse(savedRole);

            String messageUser = messageService.getMessage("roleService.save.ok", null, LocaleContextHolder.getLocale());
            return new Response<>(true, messageUser, savedRoleDto);

        }catch(DataAccessException | CannotCreateTransactionException e){
            throw new DataBaseException(e,"roleService", 0L, roleDto.getRole(), "save");

        }
    }

    private void validateRol(String roleNew){
        Optional<Role>roleOptional = roleRepository.findRoleEntityByRole(roleNew);
        if(roleOptional.isPresent()){
            Role role = roleOptional.get();
            if(role.getRole().equals(roleNew)) {
                throw new RoleExistingException("","RoleService", "Save", roleNew);
            }
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




    // Metodo para convertir la entidad Role a RoleDTO
    private RoleDTO convertToDTO(Role role) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setRole(role.getRole());
        roleDTO.setPermissionsList(role.getPermissionsList());
        return roleDTO;
    }

    // Metodo para convertir la entidad Role a RoleDTO
    private RoleResponseDTO convertToDTOResponse(Role role) {
        RoleResponseDTO roleDTO = new RoleResponseDTO();
        roleDTO.setId(role.getId());
        roleDTO.setRole(role.getRole());
        roleDTO.setPermissionsList(role.getPermissionsList());
        return roleDTO;
    }

    private Set<Permission> getPermissionForRole(Set<Permission> permissions) {
        Set<Permission> validPermission = new HashSet<>();
        for (Permission permission : permissions) {
            Permission foundPermission = permissionService.findById(permission.getId()).orElseThrow(()->
                    new PermissionNotFoundRoleCreationException("",permission.getId(),"RoleService", "Save")
            );
            validPermission.add(foundPermission);
        }
        return validPermission;
    }

    private Role buildRole(RoleDTO roleDto) {
        return Role.builder()
                .role(roleDto.getRole())
                .permissionsList(roleDto.getPermissionsList())
                .build();
    }
}
