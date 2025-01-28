package com.securitysolution.spring.security.jwt.oauth2.controller;
import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IPermissionService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@PreAuthorize("denyAll()")
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permiService;


    //Endpoint para obtener todos los roles
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    //Endpoint para obtener roles por ID.
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    //Endpoint para crear roles
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') and hasPermission('CREATE')")
    public ResponseEntity<Role> createRole(@RequestBody Role role) {

        //Declaro de  objetos y listas.
        Set<Permission> permiList = new HashSet<Permission>();
        Permission readPermission;

        // Recuperar la Permission/s por su ID
        for (Permission per : role.getPermissionsList()) {
            readPermission = permiService.findById(per.getId()).orElse(null);
            if (readPermission != null) {
                //si encuentro, guardo en la lista
                permiList.add(readPermission);
            }
        }

        //Asingo al objeto recibido en la request el permiso COMPLETO, ya que solo viene el ID.
        role.setPermissionsList(permiList);

        //Creo nueva variable para responder en el Entity
        Role newRole = roleService.save(role);
        return ResponseEntity.ok(newRole);
    }

    //Agregamos end-point de UPDATE
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {

        Role rol = roleService.findById(id).orElse(null);
        if (rol!=null) {
            rol = role;
        }

        roleService.update(rol);
        return ResponseEntity.ok(rol);

    }


}
