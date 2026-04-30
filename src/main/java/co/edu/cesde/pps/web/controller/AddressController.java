package co.edu.cesde.pps.web.controller;

import co.edu.cesde.pps.application.AddressApplicationService;
import co.edu.cesde.pps.web.security.CurrentSessionResolver;
import co.edu.cesde.pps.web.dto.request.AddressUpsertRequest;
import co.edu.cesde.pps.web.dto.response.AddressResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.USER_ADDRESSES)
@Tag(name = "Direcciones de Usuario", description = "Endpoints para gestionar direcciones de envío del usuario")
public class AddressController {

    private final AddressApplicationService addressApplicationService;
    private final CurrentSessionResolver currentSessionResolver;

    public AddressController(AddressApplicationService addressApplicationService,
                             CurrentSessionResolver currentSessionResolver) {
        this.addressApplicationService = addressApplicationService;
        this.currentSessionResolver = currentSessionResolver;
    }

    @GetMapping
    @Operation(summary = "Listar mis direcciones", description = "Retorna todas las direcciones del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de direcciones obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = AddressResponse.class)))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<AddressResponse> listMyAddresses(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader) {
        return addressApplicationService.listMyAddresses(currentSessionResolver.resolveCurrentToken(authorizationHeader));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener dirección por ID", description = "Retorna los detalles de una dirección específica del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dirección obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado a esta dirección"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public AddressResponse getMyAddress(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @Parameter(description = "ID de la dirección", example = "1", required = true)
            @PathVariable Long id) {
        return addressApplicationService.getMyAddress(currentSessionResolver.resolveCurrentToken(authorizationHeader), id);
    }

    @PostMapping
    @Operation(summary = "Agregar nueva dirección", description = "Crea una nueva dirección para el usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Dirección creada exitosamente",
            content = @Content(schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<AddressResponse> addAddress(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @RequestBody @Valid AddressUpsertRequest request) {
        AddressResponse response = addressApplicationService.addAddress(
                currentSessionResolver.resolveCurrentToken(authorizationHeader), request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar dirección", description = "Actualiza completamente los datos de una dirección existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dirección actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado a esta dirección"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public AddressResponse updateAddress(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @Parameter(description = "ID de la dirección", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody @Valid AddressUpsertRequest request) {
        return addressApplicationService.updateAddress(currentSessionResolver.resolveCurrentToken(authorizationHeader), id, request);
    }

    @PatchMapping("/{id}/default")
    @Operation(summary = "Establecer dirección por defecto", description = "Marca una dirección como dirección de envío por defecto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dirección establecida como predeterminada",
            content = @Content(schema = @Schema(implementation = AddressResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado a esta dirección"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public AddressResponse setDefaultAddress(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @Parameter(description = "ID de la dirección", example = "1", required = true)
            @PathVariable Long id) {
        return addressApplicationService.setDefaultAddress(currentSessionResolver.resolveCurrentToken(authorizationHeader), id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar dirección", description = "Elimina permanentemente una dirección del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Dirección eliminada exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado a esta dirección"),
        @ApiResponse(responseCode = "404", description = "Dirección no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @Parameter(description = "ID de la dirección", example = "1", required = true)
            @PathVariable Long id) {
        addressApplicationService.deleteAddress(currentSessionResolver.resolveCurrentToken(authorizationHeader), id);
        return ResponseEntity.noContent().build();
    }
}