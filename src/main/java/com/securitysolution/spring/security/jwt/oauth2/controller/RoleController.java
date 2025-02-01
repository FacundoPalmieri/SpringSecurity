package com.securitysolution.spring.security.jwt.oauth2.controller;
import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IPermissionService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Controlador para gestionar los roles del sistema.
 * Proporciona endpoints para listar, obtener, crear y actualizar roles.
 * Todos los endpoints requieren autenticación con rol DEV.
 */
@RestController
@PreAuthorize("denyAll()")
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IPermissionService permiService;


    /**
     * Lista todos los roles.
     * Requiere rol DEV para acceder.
     * @return Retorna 200 con la lista de roles.
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     */
    @Operation(summary = "Obtener listado de roles", description = "Lista todos los roles.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol Creado."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('DEV')")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }


    /**
     * Obtiene Rol por ID
     * Requiere rol DEV para acceder.
     *
     * @param id ID del Rol a buscar.
     * @return Retorna 200 si el rol existe, junto con sus detalles.
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     *         Retorna 404 si no lo encuentra.
     */
    @Operation(summary = "Obtener Rol", description = "Obtiene un Rol por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rol encontrado."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Rol no encontrado."),
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DEV')")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.findById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * Crea un nuevo Rol en el sistema.
     * Requiere rol DEV para acceder.
     *
     * @param role Datos del Rol a crear.
     * @return Retorna 200 si crea el rol, junto con sus detalles.
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     *         Retorna 404 si no lo encuentra.
     */
    @Operation(summary = "Crear Rol", description = "Crea un nuevo rol en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "rol Creado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Permisos requeridos no encontrados.")
    })
    @PostMapping
    @PreAuthorize("hasRole('DEV')")
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
    @PreAuthorize("hasRole('DEV')")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {

        Role rol = roleService.findById(id).orElse(null);
        if (rol!=null) {
            rol = role;
        }

        roleService.update(rol);
        return ResponseEntity.ok(rol);

    }


}
