package co.edu.cesde.pps.web.controller;

import co.edu.cesde.pps.application.CatalogApplicationService;
import co.edu.cesde.pps.exception.EntityNotFoundException;
import co.edu.cesde.pps.web.dto.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.PRODUCTS)
@Tag(name = "Productos", description = "Endpoints para consultar el catálogo de productos")
public class ProductController {

    private final CatalogApplicationService catalogApplicationService;

    public ProductController(CatalogApplicationService catalogApplicationService) {
        this.catalogApplicationService = catalogApplicationService;
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Retorna una lista de productos con opciones de búsqueda y filtrado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<ProductResponse> listProducts(
            @Parameter(description = "Término de búsqueda sobre nombre o descripción", example = "laptop")
            @RequestParam(required = false) String search,
            @Parameter(description = "ID de la categoría para filtrar productos", example = "1")
            @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Si es true, solo retorna productos activos", example = "true")
            @RequestParam(defaultValue = "true") boolean activeOnly) {
        List<ProductResponse> products = resolveBaseProducts(search, categoryId);

        return products.stream()
                .filter(product -> categoryId == null || categoryId.equals(product.categoryId()))
                .filter(product -> !activeOnly || Boolean.TRUE.equals(product.isActive()))
                .toList();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Retorna los detalles de un producto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ProductResponse getProduct(
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long id) {
        ProductResponse response = catalogApplicationService.getProduct(id);
        if (!Boolean.TRUE.equals(response.isActive())) {
            throw new EntityNotFoundException("Product", id);
        }
        return response;
    }

    private List<ProductResponse> resolveBaseProducts(String search, Long categoryId) {
        if (search != null && !search.isBlank()) {
            return catalogApplicationService.searchProducts(search);
        }
        if (categoryId != null) {
            return catalogApplicationService.listProductsByCategory(categoryId);
        }
        return catalogApplicationService.listProducts(false);
    }
}