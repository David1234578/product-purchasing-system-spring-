package co.edu.cesde.pps.web.controller;

import co.edu.cesde.pps.application.AuthApplicationService;
import co.edu.cesde.pps.web.dto.request.LoginRequest;
import co.edu.cesde.pps.web.dto.request.RegisterRequest;
import co.edu.cesde.pps.web.dto.response.AuthSessionResponse;
import co.edu.cesde.pps.web.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import co.edu.cesde.pps.web.security.CurrentSessionResolver;

@RestController
@RequestMapping(ApiRoutes.AUTH)
@Tag(name = "Autenticación", description = "Endpoints para autenticación y gestión de sesiones")
public class AuthController {

    private final AuthApplicationService authApplicationService;
    private final CurrentSessionResolver currentSessionResolver;

    public AuthController(AuthApplicationService authApplicationService,
                          CurrentSessionResolver currentSessionResolver) {
        this.authApplicationService = authApplicationService;
        this.currentSessionResolver = currentSessionResolver;
    }

    @PostMapping("/guest-session")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear sesión de invitado", description = "Crea una nueva sesión para un usuario invitado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Sesión de invitado creada exitosamente",
            content = @Content(schema = @Schema(implementation = AuthSessionResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public AuthSessionResponse createGuestSession() {
        return authApplicationService.createGuestSession();
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nuevo usuario", description = "Registra un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente",
            content = @Content(schema = @Schema(implementation = AuthSessionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o usuario ya existe"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AuthSessionResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authApplicationService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario y retorna un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso",
            content = @Content(schema = @Schema(implementation = AuthSessionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public AuthSessionResponse login(@Valid @RequestBody LoginRequest request) {
        return authApplicationService.login(request);
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener usuario actual", description = "Retorna la información del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public UserResponse getCurrentUser(
            @Parameter(description = "Token de autorización JWT en formato Bearer", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader) {
        return authApplicationService.getCurrentUser(
                currentSessionResolver.resolveCurrentToken(authorizationHeader)
        );
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Cierra la sesión del usuario actual")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sesión cerrada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> logout(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader) {
        authApplicationService.logout(currentSessionResolver.resolveCurrentToken(authorizationHeader));
        return ResponseEntity.noContent().build();
    }
}