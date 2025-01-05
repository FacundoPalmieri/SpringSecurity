package com.todocodeacademy.springsecurity.controller;

import com.todocodeacademy.springsecurity.model.Role;
import com.todocodeacademy.springsecurity.model.UserSec;
import com.todocodeacademy.springsecurity.service.interfaces.IRoleService;
import com.todocodeacademy.springsecurity.service.interfaces.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;


    //Endpoint para Obtener todos los usuarios.
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<UserSec>> getAllUsers() {
        List<UserSec> users = userService.findAll();
        return ResponseEntity.ok(users);
    }


    //Endpoint para Obtener usuario por ID.
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<UserSec> getUserById(@PathVariable Long id) {
        Optional<UserSec> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Endpoint para crear usuarios
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserSec> createUser(@Valid @RequestBody UserSec userSec) {

        //Declaración de objetos y listas
        Set<Role> roleList = new HashSet<Role>();
        Role readRole;

        //encriptado contraseña
        userSec.setPassword(userService.encriptPassword(userSec.getPassword()));

        // Recuperar los Roles por su ID
        for (Role role : userSec.getRolesList()){
            readRole = roleService.findById(role.getId()).orElse(null);
            if (readRole != null) {

                //si se encuentra, se guarda en la lista
                roleList.add(readRole);
            }
        }

        //Si la lista no está vacía guardo los roles en el objeto que vino por parámetro.
        if (!roleList.isEmpty()) {
            userSec.setRolesList(roleList);

            // Generamos una nueva isntancia de UserSec para poder enviar ese objeto en el ResponseEntity
            UserSec newUser = userService.save(userSec);
            return ResponseEntity.ok(newUser);
        }
        return null;
    }


}