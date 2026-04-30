package co.edu.cesde.pps.web.controller;

import co.edu.cesde.pps.application.CatalogApplicationService;
import co.edu.cesde.pps.web.dto.response.CategoryResponse;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.CATEGORIES)
@Tag(name = "Categorías", description = "Endpoints para consultar categorías de productos")
public class CategoryController {

    private final CatalogApplicationService catalogApplicationService;

    public CategoryController(CatalogApplicationService catalogApplicationService) {
        this.catalogApplicationService = catalogApplicationService;
    }

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Retorna una lista plana de todas las categorías")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<CategoryResponse> listCategories() {
        return catalogApplicationService.listCategories();
    }

    @GetMapping("/tree")
    @Operation(summary = "Obtener árbol de categorías", description = "Retorna las categorías organizadas en estructura jerárquica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Árbol de categorías obtenido exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<CategoryResponse> listCategoryTree() {
        return catalogApplicationService.listCategoryTree();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Retorna los detalles de una categoría específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = CategoryResponse.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CategoryResponse getCategory(
            @Parameter(description = "ID de la categoría", example = "1", required = true)
            @PathVariable Long id) {
        return catalogApplicationService.getCategory(id);
    }

    @GetMapping("/{id}/subcategories")
    @Operation(summary = "Listar subcategorías", description = "Retorna una lista de subcategorías de una categoría específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de subcategorías obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))),
        @ApiResponse(responseCode = "404", description = "Categoría padre no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<CategoryResponse> listSubcategories(
            @Parameter(description = "ID de la categoría padre", example = "1", required = true)
            @PathVariable Long id) {
        return catalogApplicationService.listSubcategories(id);
    }
}