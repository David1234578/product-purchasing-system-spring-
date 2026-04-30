package co.edu.cesde.pps.web.controller;

import co.edu.cesde.pps.application.AdminUserApplicationService;
import co.edu.cesde.pps.web.security.AdminAccessGuard;
import co.edu.cesde.pps.web.dto.request.CreateAdminUserRequest;
import co.edu.cesde.pps.web.dto.request.UpdateAdminUserRequest;
import co.edu.cesde.pps.web.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.ADMIN_USERS)
@Tag(name = "Admin - Usuarios", description = "Endpoints administrativos para gestionar usuarios del sistema")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    private final AdminUserApplicationService adminUserApplicationService;
    private final AdminAccessGuard adminAccessGuard;

    public AdminUserController(AdminUserApplicationService adminUserApplicationService,
                               AdminAccessGuard adminAccessGuard) {
        this.adminUserApplicationService = adminUserApplicationService;
        this.adminAccessGuard = adminAccessGuard;
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema (solo administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requieren permisos de administrador"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "Token de autorización JWT en formato Bearer (solo administrador)")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
            @RequestBody @Valid CreateAdminUserRequest request) {
        adminAccessGuard.requireAdmin(authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUserApplicationService.createUser(request));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Retorna una lista de todos los usuarios del sistema (solo administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requieren permisos de administrador"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<UserResponse> listUsers(
            @Parameter(description = "Token de autorización JWT en formato Bearer (solo administrador)")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        adminAccessGuard.requireAdmin(authorizationHeader);
        return adminUserApplicationService.listUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Retorna los detalles de un usuario específico (solo administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requieren permisos de administrador"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public UserResponse getUser(
            @Parameter(description = "Token de autorización JWT en formato Bearer (solo administrador)")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long id) {
        adminAccessGuard.requireAdmin(authorizationHeader);
        return adminUserApplicationService.getUser(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza completamente los datos de un usuario (solo administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requieren permisos de administrador"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public UserResponse updateUser(
            @Parameter(description = "Token de autorización JWT en formato Bearer (solo administrador)")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody @Valid UpdateAdminUserRequest request) {
        adminAccessGuard.requireAdmin(authorizationHeader);
        return adminUserApplicationService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina permanentemente un usuario del sistema (solo administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requieren permisos de administrador"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Token de autorización JWT en formato Bearer (solo administrador)")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
            @Parameter(description = "ID del usuario", example = "1", required = true)
            @PathVariable Long id) {
        adminAccessGuard.requireAdmin(authorizationHeader);
        adminUserApplicationService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}