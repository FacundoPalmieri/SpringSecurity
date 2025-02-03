package com.securitysolution.spring.security.jwt.oauth2.controller;
import com.securitysolution.spring.security.jwt.oauth2.dto.Response;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecDTO;
import com.securitysolution.spring.security.jwt.oauth2.dto.UserSecResponseDTO;
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
 * <p>Controlador para gestionar los usuarios del sistema.</p>
 * <p>Proporciona endpoints para listar, obtener y crear usuarios.</p>
 * <p>Todos los endpoints requieren autenticación con rol <b>ADMIN</b>.</p>
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
     * <p>Requiere rol <b>ADMIN</b> para acceder.</p>
     * @return ResponseEntity con:
     * <ul>
     *     <li><b>200 OK</b> Lista de usuarios recuperada exitosamente.</li>
     *     <li><b>401 Unauthorized</b>: No autenticado.</li>
     *     <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     * </ul>
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
     /**
     * Obtiene un usuario por su ID.
     * <p>
     * Requiere el rol <b>ADMIN</b> para acceder.
     * </p>
     *
     * @param id ID del usuario a buscar.
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>200 OK</b>: Usuario encontrado exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         <li><b>404 Not Found</b>: Usuario no encontrado.</li>
     *         </ul>
     */
    @Operation(summary = "Obtener usuario", description = "Obtiene un usuario por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente."),
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
     * <p>
     * Requiere el rol <b>ADMIN</b> para acceder.
     * </p>
     *
     * @param userSecDto Datos del usuario a crear.
     * @return ResponseEntity con:
     *         <ul>
     *         <li><b>201 Created</b>: Usuario creado exitosamente.</li>
     *         <li><b>401 Unauthorized</b>: No autenticado.</li>
     *         <li><b>403 Forbidden</b>: No autorizado para acceder a este recurso.</li>
     *         <li><b>404 Not Found</b>: Roles y/o permisos requeridos no encontrados.</li>
     *         <li><b>409 Conflict</b>: Usuario existente en el sistema.</li>
     *         </ul>
     */

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario Creado exitosamente."),
            @ApiResponse(responseCode = "401", description = "No autenticado."),
            @ApiResponse(responseCode = "403", description = "No autorizado para acceder a este recurso."),
            @ApiResponse(responseCode = "404", description = "Roles y/o permisos requeridos no encontrados."),
            @ApiResponse(responseCode = "409", description = "Usuario existente en el sistema.")
    })
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public  ResponseEntity<Response<UserSecResponseDTO>> createUser(@Valid @RequestBody UserSecDTO userSecDto) {
        Response<UserSecResponseDTO>response = userService.save(userSecDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}