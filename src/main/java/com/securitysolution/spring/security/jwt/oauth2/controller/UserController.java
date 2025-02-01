package com.securitysolution.spring.security.jwt.oauth2.controller;

import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecDTO;
import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IRoleService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para gestionar los usuarios del sistema.
 * Proporciona endpoints para listar, obtener y crear usuarios.
 *
 * Todos los endpoints requieren autenticación con rol ADMIN.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    /**
     * Lista todos los usuarios.
     * Requiere rol ADMIN para acceder.
     * @return Retorna 200 con la lista de usuarios
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     */
    @Operation(summary = "Obtener listado de usuarios", description = "Lista todos los usuarios.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario Creado."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<UserSec>> getAllUsers() {
        List<UserSec> users = userService.findAll();
        return ResponseEntity.ok(users);
    }


    /**
     * Obtiene Usuario por ID
     * Requiere rol ADMIN para acceder.
     *
     * @param id ID del usuario a buscar.
     * @return Retorna 200 si el usuario existe, junto con sus detalles.
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     *         Retorna 404 si no encuentra roles o permisos asociados.
     */
    @Operation(summary = "Obtener usuario", description = "Obtiene un usuario por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario Creado."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DEV')")
    public ResponseEntity<UserSec> getUserById(@PathVariable Long id) {
        Optional<UserSec> user = userService.findById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * Requiere rol ADMIN para acceder.
     *
     * @param userSecDto Datos del usuario a crear.
     * @return Retorna 200 si crea el usuario, junto con sus detalles.
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     *         Retorna 404 si no encuentra roles o permisos asociados.
     */
    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario Creado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Roles y/o permisos requeridos no encontrados.")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('DEV')")
    public  ResponseEntity<Response<UserSec>> createUser(@Valid @RequestBody UserSecDTO userSecDto) {
        return userService.save(userSecDto);
    }
}