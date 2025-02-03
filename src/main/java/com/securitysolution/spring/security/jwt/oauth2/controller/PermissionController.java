package com.securitysolution.spring.security.jwt.oauth2.controller;

import com.securitysolution.spring.security.jwt.oauth2.dto.PermissionResponseDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.model.Permission;
import com.securitysolution.spring.security.jwt.oauth2.service.interfaces.IPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    /**
     * Lista todos los permisos-
     * Requiere rol ADMIN para acceder.
     * @return retorna 200 con la lista de permisos.
     *         Retorna 401 si no está autenticado.
     *         Retorna 403 si no está autorizado.
     */
    @Operation(summary = "Obtener listado de Permisos", description = "Lista todos los permisos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado recuperado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
    })
    @GetMapping("get/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<List<PermissionResponseDTO>>> getAllPermissions() {
        Response<List<PermissionResponseDTO>> response = permissionService.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    //Endpoint para obtener permisos por ID.
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Permission> getPermissionById(@PathVariable Long id) {
        Optional<Permission> permission = permissionService.findById(id);
        return permission.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // Endpoint para crear permisos.
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission newPermission = permissionService.save(permission);
        return ResponseEntity.ok(newPermission);
    }


}