# 📖 Ejemplos de Anotaciones Swagger Implementadas

## 1️⃣ Ejemplo: AuthController - Iniciar Sesión

```java
@PostMapping("/login")
@Operation(
    summary = "Iniciar sesión",
    description = "Autentica un usuario y retorna un token JWT"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Inicio de sesión exitoso",
        content = @Content(schema = @Schema(implementation = AuthSessionResponse.class))
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Credenciales inválidas"
    ),
    @ApiResponse(
        responseCode = "500",
        description = "Error interno del servidor"
    )
})
public AuthSessionResponse login(
    @Valid @RequestBody LoginRequest request
) {
    return authApplicationService.login(request);
}
```

### En Swagger UI se ve así:
```
┌─ POST /api/v1/auth/login │ Iniciar sesión ───────────────────┐
│ Autentica un usuario y retorna un token JWT                   │
├──────────────────────────────────────────────────────────────┤
│ Parameters:                                                    │
│   Body (LoginRequest)                                          │
│     - email: string (required)                                │
│     - password: string (required)                             │
│                                                                │
│ Responses:                                                     │
│   200 ✓ Inicio de sesión exitoso                              │
│         {AuthSessionResponse}                                  │
│   400 ✗ Credenciales inválidas                                │
│   500 ✗ Error interno del servidor                            │
└──────────────────────────────────────────────────────────────┘
```

---

## 2️⃣ Ejemplo: ProductController - Listar Productos

```java
@GetMapping
@Operation(
    summary = "Listar productos",
    description = "Retorna una lista de productos con opciones de búsqueda y filtrado"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Lista de productos obtenida exitosamente",
        content = @Content(
            array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))
        )
    ),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public List<ProductResponse> listProducts(
    @Parameter(description = "Término de búsqueda sobre nombre o descripción", example = "laptop")
    @RequestParam(required = false) String search,
    
    @Parameter(description = "ID de la categoría para filtrar productos", example = "1")
    @RequestParam(required = false) Long categoryId,
    
    @Parameter(description = "Si es true, solo retorna productos activos", example = "true")
    @RequestParam(defaultValue = "true") boolean activeOnly
) {
    // Implementación...
}
```

### @Parameter para cada parámetro

El `@Parameter` documenta cada parámetro con:
- **description** - Explicación del parámetro
- **example** - Valor de ejemplo
- El tipo se infiere del tipo Java

---

## 3️⃣ Ejemplo: CartController - Agregar al Carrito

```java
@PostMapping("/items")
@Operation(
    summary = "Agregar producto al carrito",
    description = "Agrega un nuevo producto o incrementa la cantidad si ya existe"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Producto agregado exitosamente",
        content = @Content(schema = @Schema(implementation = CartResponse.class))
    ),
    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
    @ApiResponse(responseCode = "401", description = "No autenticado"),
    @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
public CartResponse addItem(
    @Parameter(description = "Token de autorización JWT en formato Bearer")
    @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
    String authorizationHeader,
    
    @RequestBody @Valid AddCartItemRequest request
) {
    return cartApplicationService.addItem(
        currentSessionResolver.resolveCurrentToken(authorizationHeader),
        request
    );
}
```

### Notas:
- `@Parameter` también documentan headers
- `@RequestBody` con `@Valid` se documenta automáticamente
- La estructura del DTO se muestra en el schema

---

## 4️⃣ Ejemplo: AdminProductController - Con Seguridad

```java
@RestController
@RequestMapping(ApiRoutes.ADMIN_PRODUCTS)
@Tag(name = "Admin - Productos", description = "Endpoints administrativos...")
@SecurityRequirement(name = "bearerAuth")  // 👈 Requiere autenticación
public class AdminProductController {

    @PostMapping
    @Operation(summary = "Crear nuevo producto", description = "...")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "...", 
                    content = @Content(schema = @Schema(implementation = ProductResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "401", description = "No autenticado"),
        @ApiResponse(responseCode = "403", description = "No autorizado - se requieren permisos de admin"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ProductResponse> createProduct(
        @Parameter(description = "Token de autorización JWT en formato Bearer (solo admin)")
        @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false)
        String authorizationHeader,
        
        @RequestBody @Valid ProductUpsertRequest request
    ) {
        adminAccessGuard.requireAdmin(authorizationHeader);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(catalogApplicationService.createProduct(request));
    }
}
```

### En Swagger UI:
- Un candado 🔐 aparece junto al endpoint indicando que requiere autenticación
- El botón "Authorize" está disponible

---

## 5️⃣ Ejemplo: AddressController - PathVariable

```java
@GetMapping("/{id}")
@Operation(
    summary = "Obtener dirección por ID",
    description = "Retorna los detalles de una dirección específica"
)
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Dirección obtenida exitosamente",
        content = @Content(schema = @Schema(implementation = AddressResponse.class))
    ),
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
    @PathVariable Long id
) {
    return addressApplicationService.getMyAddress(
        currentSessionResolver.resolveCurrentToken(authorizationHeader),
        id
    );
}
```

### @Parameter para cada tipo:

| Tipo | Anotación | Ejemplo |
|------|-----------|---------|
| Path | `@PathVariable` | `/addresses/{id}` |
| Query | `@RequestParam` | `?search=laptop` |
| Header | `@RequestHeader` | `Authorization: Bearer ...` |

---

## 6️⃣ Ejemplo: Completo - CategoryController

```java
@RestController
@RequestMapping(ApiRoutes.CATEGORIES)
@Tag(
    name = "Categorías",
    description = "Endpoints para consultar categorías de productos"
)
public class CategoryController {

    @GetMapping
    @Operation(
        summary = "Listar categorías",
        description = "Retorna una lista plana de todas las categorías"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de categorías obtenida exitosamente",
            content = @Content(
                array = @ArraySchema(
                    schema = @Schema(implementation = CategoryResponse.class)
                )
            )
        ),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public List<CategoryResponse> listCategories() {
        return catalogApplicationService.listCategories();
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener categoría por ID",
        description = "Retorna los detalles de una categoría específica"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Categoría obtenida exitosamente",
            content = @Content(
                schema = @Schema(implementation = CategoryResponse.class)
            )
        ),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public CategoryResponse getCategory(
        @Parameter(description = "ID de la categoría", example = "1", required = true)
        @PathVariable Long id
    ) {
        return catalogApplicationService.getCategory(id);
    }
}
```

---

## 📊 Estructura de Respuesta en Swagger

### Single Object
```java
@Content(schema = @Schema(implementation = ProductResponse.class))
```
Result:
```json
{
  "id": 1,
  "name": "Laptop",
  "price": 1299.99,
  ...
}
```

### Array of Objects
```java
@Content(
    array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))
)
```
Result:
```json
[
  { "id": 1, "name": "Laptop", ... },
  { "id": 2, "name": "Monitor", ... },
  ...
]
```

---

## 🎯 Patrón de Anotaciones por Tipo de Operación

### ✅ GET (Lectura)

```java
@GetMapping("/{id}")
@Operation(summary = "Obtener por ID")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Éxito", 
                content = @Content(schema = @Schema(implementation = Response.class))),
    @ApiResponse(responseCode = "404", description = "No encontrado")
})
public Response getById(
    @Parameter(description = "ID", example = "1", required = true)
    @PathVariable Long id
) { ... }
```

### ➕ POST (Crear)

```java
@PostMapping
@Operation(summary = "Crear nuevo recurso")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Creado", 
                content = @Content(schema = @Schema(implementation = Response.class))),
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
})
public ResponseEntity<Response> create(
    @RequestBody @Valid CreateRequest request
) { ... }
```

### ✏️ PUT (Actualizar)

```java
@PutMapping("/{id}")
@Operation(summary = "Actualizar recurso")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Actualizado",
                content = @Content(schema = @Schema(implementation = Response.class))),
    @ApiResponse(responseCode = "404", description = "No encontrado")
})
public Response update(
    @Parameter(description = "ID", example = "1", required = true)
    @PathVariable Long id,
    @RequestBody @Valid UpdateRequest request
) { ... }
```

### 🗑️ DELETE (Eliminar)

```java
@DeleteMapping("/{id}")
@Operation(summary = "Eliminar recurso")
@ApiResponses(value = {
    @ApiResponse(responseCode = "204", description = "Eliminado"),
    @ApiResponse(responseCode = "404", description = "No encontrado")
})
public ResponseEntity<Void> delete(
    @Parameter(description = "ID", example = "1", required = true)
    @PathVariable Long id
) { ... }
```

---

## 🔐 Configuración de Seguridad (OpenApiConfig.java)

```java
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Product Purchasing System API",
        version = "1.0",
        description = "API RESTful para el sistema de compra de productos"
    ),
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Token JWT para autenticación"
)
public class OpenApiConfig {
}
```

### Resultado en Swagger UI:
- ✅ Botón "Authorize" visible
- ✅ Ingresa token como: `Bearer {token}`
- ✅ Token se incluye automáticamente en headers

---

## 📋 Checklist de Documentación

Para cada nuevo endpoint, verifica:

- ✅ `@Operation` con summary y description
- ✅ `@ApiResponse` para código 200/201/204
- ✅ `@ApiResponse` para código 400 (si acepta datos)
- ✅ `@ApiResponse` para código 401 (si requiere auth)
- ✅ `@ApiResponse` para código 403 (si requiere admin)
- ✅ `@ApiResponse` para código 404 (si usa PathVariable)
- ✅ `@ApiResponse` para código 500
- ✅ `@Parameter` para cada parámetro con description y example
- ✅ `@Content` con `@Schema` vinculando DTO
- ✅ `@ArraySchema` si retorna lista
- ✅ `@SecurityRequirement` si requiere autenticación

---

## ✨ Conclusión

Cada anotación tiene un propósito específico:

| Anotación | Cuándo usarla |
|-----------|---------------|
| `@Tag` | En la clase del controlador |
| `@Operation` | En cada método HTTP |
| `@ApiResponses` | Para documentar múltiples respuestas |
| `@ApiResponse` | Dentro de `@ApiResponses` |
| `@Parameter` | En cada parámetro del método |
| `@Content` | Dentro de `@ApiResponse` |
| `@Schema` | Para vincular DTOs |
| `@ArraySchema` | Cuando es lista de objetos |
| `@SecurityRequirement` | Si requiere JWT |

**¡Así tu API está completamente autodocumentada! 🎉**

