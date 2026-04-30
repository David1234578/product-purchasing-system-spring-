package co.edu.cesde.pps.web.controller;

import co.edu.cesde.pps.application.CatalogApplicationService;
import co.edu.cesde.pps.web.security.AdminAccessGuard;
import co.edu.cesde.pps.web.dto.request.ProductUpsertRequest;
import co.edu.cesde.pps.web.dto.response.ProductResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.ADMIN_PRODUCTS)
@Tag(name = "Admin - Productos", description = "Endpoints administrativos para gestionar el catálogo de productos")
@SecurityRequirement(name = "bearerAuth")
public class AdminProductController {

    private final CatalogApplicationService catalogApplicationService;
    private final AdminAccessGuard adminAccessGuard;

    public AdminProductController(CatalogApplicationService catalogApplicationService,
                                  AdminAccessGuard adminAccessGuard) {
        this.catalogApplicationService = catalogApplicationService;
        this.adminAccessGuard = adminAccessGuard;
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el sistema (solo administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requieren permisos de administrador"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ProductResponse> createProduct(
            @Parameter(description = "Token de autorización JWT en formato Bearer (solo administrador)")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
            @RequestBody @Valid ProductUpsertRequest request) {
        adminAccessGuard.requireAdmin(authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogApplicationService.createProduct(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto", description = "Actualiza completamente los datos de un producto (solo administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requieren permisos de administrador"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ProductResponse updateProduct(
            @Parameter(description = "Token de autorización JWT en formato Bearer (solo administrador)")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody @Valid ProductUpsertRequest request) {
        adminAccessGuard.requireAdmin(authorizationHeader);
        return catalogApplicationService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto", description = "Elimina permanentemente un producto del sistema (solo administradores)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requieren permisos de administrador"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "Token de autorización JWT en formato Bearer (solo administrador)")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long id) {
        adminAccessGuard.requireAdmin(authorizationHeader);
        catalogApplicationService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}