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
 * <p> Proporciona endpoints para listar, obtener, crear y actualizar roles. </p>
 * Todos los endpoints requieren autenticación con rol <b>DEV</b>.
 *
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
     * Lista todos los roles disponibles en el sistema.
     * <p>
     * Requiere el rol <b>DEV</b> para acceder.
     * </p>
     *
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Listado de roles recuperado exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         </ul>
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
     * Obtiene un rol por su ID.
     * <p>
     * Requiere el rol <b>DEV</b> para acceder.
     * </p>
     *
     * @param id ID del rol a buscar.
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Rol encontrado exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         <li><b>404 Not Found</b>: Rol no encontrado.</li>
     *         </ul>
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
     * Crea un nuevo rol en el sistema.
     * <p>
     * Requiere el rol <b>DEV</b> para acceder.
     * </p>
     *
     * @param roleDto Datos del rol a crear.
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Rol creado exitosamente.</li>
     *         <li><b>400 Bad Request</b>: No se encuentran los permisos asociados.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         <li><b>409 Conflict</b>: Rol existente en el sistema.</li>
     *         </ul>
     */

    @Operation(summary = "Crear Rol", description = "Crea un nuevo rol en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "rol Creado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Permisos requeridos no encontrados."),
            @ApiResponse(responseCode = "409", description = "Rol existente en el sistema.")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('DEV')")
    public ResponseEntity<Response<RoleResponseDTO>>createRole(@Valid @RequestBody RoleDTO roleDto) {
        Response<RoleResponseDTO> response = roleService.save(roleDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }




}
