package com.securitysolution.spring.security.jwt.oauth2.controller;

import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.model.UserSec;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IRoleService;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * Controlador para gestionar los usuarios del sistema.
 * Proporciona endpoints para listar, obtener y crear usuarios.
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
    @GetMapping("/get/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<List<UserSecResponseDTO>>> getAllUsers() {
        Response<List<UserSecResponseDTO>> response = userService.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Obtiene Usuario por ID
     * Requiere rol ADMIN para acceder.
     *
     * @param id ID del usuario a buscar.
     * @return Retorna 200 si el usuario existe, junto con sus detalles.
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     *         Retorna UserNotFound si no es encontrado.
     */
    @Operation(summary = "Obtener usuario", description = "Obtiene un usuario por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario Creado."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<UserSecResponseDTO>> getUserById(@PathVariable Long id) {
        Response<UserSecResponseDTO>response = userService.findById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);

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
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<Response<UserSecResponseDTO>> createUser(@Valid @RequestBody UserSecDTO userSecDto) {
        Response<UserSecResponseDTO>response = userService.save(userSecDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}