package co.edu.cesde.pps.web.controller;

import co.edu.cesde.pps.application.UserProfileApplicationService;
import co.edu.cesde.pps.web.security.CurrentSessionResolver;
import co.edu.cesde.pps.web.dto.request.ChangeMyPasswordRequest;
import co.edu.cesde.pps.web.dto.request.UpdateMyProfileRequest;
import co.edu.cesde.pps.web.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.USER_PROFILE)
@Tag(name = "Perfil de Usuario", description = "Endpoints para gestionar el perfil del usuario autenticado")
@SecurityRequirement(name = "bearerAuth")
public class UserProfileController {

    private final UserProfileApplicationService userProfileApplicationService;
    private final CurrentSessionResolver currentSessionResolver;

    public UserProfileController(UserProfileApplicationService userProfileApplicationService,
                                 CurrentSessionResolver currentSessionResolver) {
        this.userProfileApplicationService = userProfileApplicationService;
        this.currentSessionResolver = currentSessionResolver;
    }

    @PutMapping
    @Operation(summary = "Actualizar perfil", description = "Actualiza la información personal del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public UserResponse updateMyProfile(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @RequestBody @Valid UpdateMyProfileRequest request) {
        return userProfileApplicationService.updateMyProfile(
                currentSessionResolver.resolveCurrentToken(authorizationHeader),
                request
        );
    }

    @PutMapping("/password")
    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Contraseña cambiada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta o datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> changeMyPassword(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @RequestBody @Valid ChangeMyPasswordRequest request) {
        userProfileApplicationService.changeMyPassword(
                currentSessionResolver.resolveCurrentToken(authorizationHeader),
                request
        );
        return ResponseEntity.noContent().build();
    }
}