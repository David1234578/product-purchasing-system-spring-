package co.edu.cesde.pps.web.controller;

import co.edu.cesde.pps.application.OrderApplicationService;
import co.edu.cesde.pps.web.security.CurrentSessionResolver;
import co.edu.cesde.pps.web.dto.request.CheckoutRequest;
import co.edu.cesde.pps.web.dto.response.OrderResponse;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRoutes.ORDERS)
@Tag(name = "Órdenes de Compra", description = "Endpoints para gestionar órdenes de compra")
public class OrderController {

    private final OrderApplicationService orderApplicationService;
    private final CurrentSessionResolver currentSessionResolver;

    public OrderController(OrderApplicationService orderApplicationService,
                           CurrentSessionResolver currentSessionResolver) {
        this.orderApplicationService = orderApplicationService;
        this.currentSessionResolver = currentSessionResolver;
    }

    @PostMapping("/checkout")
    @Operation(summary = "Procesar compra", description = "Realiza el checkout del carrito y crea una nueva orden")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Orden creada exitosamente",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o carrito vacío"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<OrderResponse> checkout(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @RequestBody @Valid CheckoutRequest request) {
        OrderResponse response = orderApplicationService.checkout(
                currentSessionResolver.resolveCurrentToken(authorizationHeader),
                request
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Listar mis órdenes", description = "Retorna todas las órdenes del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida exitosamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponse.class)))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<OrderResponse> listMyOrders(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader) {
        return orderApplicationService.listMyOrders(currentSessionResolver.resolveCurrentToken(authorizationHeader));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID", description = "Retorna los detalles de una orden específica del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Orden obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = OrderResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado a esta orden"),
        @ApiResponse(responseCode = "404", description = "Orden no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public OrderResponse getMyOrder(
            @Parameter(description = "Token de autorización JWT en formato Bearer")
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
            String authorizationHeader,
            @Parameter(description = "ID de la orden", example = "1", required = true)
            @PathVariable Long id) {
        return orderApplicationService.getMyOrder(currentSessionResolver.resolveCurrentToken(authorizationHeader), id);
    }
}