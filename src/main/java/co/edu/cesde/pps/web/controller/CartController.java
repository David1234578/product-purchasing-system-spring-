package co.edu.cesde.pps.web.controller;

import co.edu.cesde.pps.application.CartApplicationService;
import co.edu.cesde.pps.web.security.CurrentSessionResolver;
import co.edu.cesde.pps.web.dto.request.AddCartItemRequest;
import co.edu.cesde.pps.web.dto.request.MergeGuestCartRequest;
import co.edu.cesde.pps.web.dto.request.UpdateCartItemQuantityRequest;
import co.edu.cesde.pps.web.dto.response.CartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.CART)
@Tag(name = "Carrito de Compras", description = "Endpoints para gestionar el carrito de compras")
public class CartController {

    private final CartApplicationService cartApplicationService;
    private final CurrentSessionResolver currentSessionResolver;

    public CartController(CartApplicationService cartApplicationService,
                          CurrentSessionResolver currentSessionResolver) {
        this.cartApplicationService = cartApplicationService;
        this.currentSessionResolver = currentSessionResolver;
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener carrito actual", description = "Retorna el carrito de compras del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CartResponse getCurrentCart(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader) {
        return cartApplicationService.getCurrentCart(currentSessionResolver.resolveCurrentToken(authorizationHeader));
    }

    @PostMapping("/items")
    @Operation(summary = "Agregar producto al carrito", description = "Agrega un nuevo producto o incrementa la cantidad si ya existe")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto agregado exitosamente",
            content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CartResponse addItem(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @RequestBody @Valid AddCartItemRequest request) {
        return cartApplicationService.addItem(currentSessionResolver.resolveCurrentToken(authorizationHeader), request);
    }

    @PatchMapping("/items/{productId}")
    @Operation(summary = "Actualizar cantidad de producto", description = "Actualiza la cantidad de un producto en el carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cantidad actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o cantidad fuera de rango"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CartResponse updateItemQuantity(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long productId,
            @RequestBody @Valid UpdateCartItemQuantityRequest request) {
        return cartApplicationService.updateItemQuantity(
                currentSessionResolver.resolveCurrentToken(authorizationHeader),
                productId,
                request
        );
    }

    @DeleteMapping("/items/{productId}")
    @Operation(summary = "Eliminar producto del carrito", description = "Remueve completamente un producto del carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado exitosamente",
            content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado en el carrito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CartResponse removeItem(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @Parameter(description = "ID del producto", example = "1", required = true)
            @PathVariable Long productId) {
        return cartApplicationService.removeItem(currentSessionResolver.resolveCurrentToken(authorizationHeader), productId);
    }

    @DeleteMapping("/items")
    @Operation(summary = "Limpiar carrito", description = "Elimina todos los productos del carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Carrito vaciado exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> clearCurrentCart(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader) {
        cartApplicationService.clearCurrentCart(currentSessionResolver.resolveCurrentToken(authorizationHeader));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/merge")
    @Operation(summary = "Fusionar carrito de invitado", description = "Fusiona el carrito de invitado con el carrito del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carritos fusionados exitosamente",
            content = @Content(schema = @Schema(implementation = CartResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CartResponse mergeGuestCart(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @RequestBody @Valid MergeGuestCartRequest request) {
        return cartApplicationService.mergeGuestCart(currentSessionResolver.resolveCurrentToken(authorizationHeader), request);
    }
}