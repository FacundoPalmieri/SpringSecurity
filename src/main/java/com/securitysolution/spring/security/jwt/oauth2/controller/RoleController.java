package com.securitysolution.spring.security.jwt.oauth2.controller;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.RoleDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.RoleResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.model.Role;
import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IPermissionService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            @ApiResponse(responseCode = "200", description = "Listado recuperado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
    })
    @GetMapping("/get/all")
    @PreAuthorize("hasAnyRole('DEV')")
    public ResponseEntity<Response<List<Role>>> getAllRoles() {
        Response<List<Role>> response = roleService.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Obtiene Rol por ID
     * Requiere rol DEV para acceder.
     *
     * @param id ID del Rol a buscar.
     * @return Retorna 200 si el rol existe, junto con sus detalles.
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     *         Retorna 400 si no lo encuentra.
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
    public ResponseEntity<Response<RoleResponseDTO>> getRoleById(@Valid @PathVariable Long id) {
        Response<RoleResponseDTO> response = roleService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Crea un nuevo Rol en el sistema.
     * Requiere rol DEV para acceder.
     *
     * @param roleDto Datos del Rol a crear.
     * @return Retorna 200 si crea el rol, junto con sus detalles.
     *         Retorna 400 si no encuentra permisos asociados.
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     */
    @Operation(summary = "Crear Rol", description = "Crea un nuevo rol en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "rol Creado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Permisos requeridos no encontrados.")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('DEV')")
    public ResponseEntity<Response<RoleResponseDTO>>createRole(@Valid @RequestBody RoleDTO roleDto) {
        Response<RoleResponseDTO> response = roleService.save(roleDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




}
