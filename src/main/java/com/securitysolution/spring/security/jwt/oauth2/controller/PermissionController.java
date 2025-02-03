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

/**
 * Controlador para la gestión de permisos.
 * <p>Proporciona endpoints para listar y obtener permisos por ID.</p>
 *
 * Todos los endpoints requieren autenticación con rol DEV.
 */
@RestController
@PreAuthorize("denyAll()")
@RequestMapping("/api/permissions")
public class PermissionController {

    @Autowired
    private IPermissionService permissionService;

    /**
     * Lista todos los permisos disponibles en el sistema.
     * <p>
     * Requiere el rol <b>DEV</b> para acceder.
     * </p>
     *
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Listado de permisos recuperado exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         </ul>
     */
    @Operation(summary = "Obtener listado de Permisos", description = "Lista todos los permisos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado recuperado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
    })
    @GetMapping("get/all")
    @PreAuthorize("hasAnyRole('DEV')")
    public ResponseEntity<Response<List<PermissionResponseDTO>>> getAllPermissions() {
        Response<List<PermissionResponseDTO>> response = permissionService.findAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Obtiene un permiso por su ID.
     * <p>
     * Requiere el rol <b>DEV</b> para acceder.
     * </p>
     *
     * @param id ID del permiso a buscar.
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Permiso encontrado exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         <li><b>404 Not Found</b>: Permiso no encontrado.</li>
     *         </ul>
     */
    @Operation(summary = "Obtener Permiso", description = "Obtiene un permiso por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Permiso encontrado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado.")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('DEV')")
    public ResponseEntity<Response<PermissionResponseDTO>> getPermissionById(@PathVariable Long id) {
        Response<PermissionResponseDTO> response = permissionService.getById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*

    // Endpoint para crear permisos.
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission newPermission = permissionService.save(permission);
        return ResponseEntity.ok(newPermission);
    }

     */


}