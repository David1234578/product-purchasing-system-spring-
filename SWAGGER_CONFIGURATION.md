# Configuración de Swagger/OpenAPI

## Descripción General
Se ha implementado una documentación completa de la API REST utilizando **SpringDoc OpenAPI 2.8.8** con Swagger UI integrado.

## Anotaciones Utilizadas

### 1. **@Tag**
Define categorías o etiquetas para agrupar endpoints relacionados.

```java
@Tag(name = "Autenticación", description = "Endpoints para autenticación y gestión de sesiones")
```

**Controladores configurados con @Tag:**
- `AuthController` → "Autenticación"
- `ProductController` → "Productos"
- `CategoryController` → "Categorías"
- `CartController` → "Carrito de Compras"
- `OrderController` → "Órdenes de Compra"
- `AddressController` → "Direcciones de Usuario"
- `UserProfileController` → "Perfil de Usuario"
- `AdminProductController` → "Admin - Productos"
- `AdminUserController` → "Admin - Usuarios"

### 2. **@Operation**
Documenta cada operación de API con un resumen y descripción detallada.

```java
@Operation(summary = "Listar productos", description = "Retorna una lista de productos con opciones de búsqueda y filtrado")
```

### 3. **@ApiResponses y @ApiResponse**
Documentan todas las respuestas posibles de un endpoint incluyendo códigos de estado HTTP.

```java
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Éxito", 
        content = @Content(schema = @Schema(implementation = ProductResponse.class))),
    @ApiResponse(responseCode = "404", description = "No encontrado"),
    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
})
```

**Códigos HTTP documentados:**
- `200` - OK / Éxito
- `201` - Created / Recurso creado
- `204` - No Content / Operación exitosa sin contenido
- `400` - Bad Request / Datos inválidos
- `401` - Unauthorized / No autenticado
- `403` - Forbidden / No autorizado
- `404` - Not Found / Recurso no encontrado
- `500` - Internal Server Error / Error del servidor

### 4. **@Parameter**
Documenta parámetros de entrada con descripción, tipo y ejemplos.

```java
@Parameter(description = "ID del producto", example = "1", required = true)
@PathVariable Long id
```

**Tipos de parámetros documentados:**
- Path parameters (`@PathVariable`)
- Query parameters (`@RequestParam`)
- Headers (`@RequestHeader`)
- Request body

### 5. **@Content**
Especifica el contenido de las respuestas.

```java
@Content(schema = @Schema(implementation = ProductResponse.class))
```

### 6. **@Schema**
Define esquemas de datos para request/response.

```java
@Schema(implementation = ProductResponse.class)
```

### 7. **@ArraySchema**
Documenta arrays de elementos.

```java
@Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class)))
```

## Configuración de Seguridad

### OpenApiConfig.java
Archivo de configuración centralizado que define:

```java
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Token JWT para autenticación"
)
```

## Acceso a Swagger UI

Una vez que el servidor esté en ejecución, accede a la documentación interactiva en:

```
http://localhost:8080/swagger-ui.html
```

Alternativas:
- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/v3/api-docs` (JSON del OpenAPI)
- `http://localhost:8080/v3/api-docs.yaml` (YAML del OpenAPI)

## Estructura de Respuestas Documentadas

### Autenticación (Auth)
- `POST /api/v1/auth/guest-session` - Crear sesión de invitado
- `POST /api/v1/auth/register` - Registrar nuevo usuario
- `POST /api/v1/auth/login` - Iniciar sesión
- `GET /api/v1/auth/me` - Obtener usuario actual
- `POST /api/v1/auth/logout` - Cerrar sesión

### Productos (Público)
- `GET /api/v1/products` - Listar productos con filtros
- `GET /api/v1/products/{id}` - Obtener producto por ID

### Categorías (Público)
- `GET /api/v1/categories` - Listar categorías
- `GET /api/v1/categories/tree` - Obtener árbol jerárquico
- `GET /api/v1/categories/{id}` - Obtener categoría por ID
- `GET /api/v1/categories/{id}/subcategories` - Listar subcategorías

### Carrito (Autenticado)
- `GET /api/v1/cart/me` - Obtener carrito actual
- `POST /api/v1/cart/items` - Agregar producto
- `PATCH /api/v1/cart/items/{productId}` - Actualizar cantidad
- `DELETE /api/v1/cart/items/{productId}` - Eliminar producto
- `DELETE /api/v1/cart/items` - Limpiar carrito
- `POST /api/v1/cart/merge` - Fusionar carrito de invitado

### Órdenes (Autenticado)
- `POST /api/v1/orders/checkout` - Procesar compra
- `GET /api/v1/orders/me` - Listar mis órdenes
- `GET /api/v1/orders/{id}` - Obtener orden específica

### Direcciones (Autenticado)
- `GET /api/v1/users/me/addresses` - Listar direcciones
- `GET /api/v1/users/me/addresses/{id}` - Obtener dirección
- `POST /api/v1/users/me/addresses` - Agregar dirección
- `PUT /api/v1/users/me/addresses/{id}` - Actualizar dirección
- `PATCH /api/v1/users/me/addresses/{id}/default` - Establecer predeterminada
- `DELETE /api/v1/users/me/addresses/{id}` - Eliminar dirección

### Perfil de Usuario (Autenticado)
- `PUT /api/v1/users/me` - Actualizar perfil
- `PUT /api/v1/users/me/password` - Cambiar contraseña

### Admin - Productos (Solo Admin)
- `POST /api/v1/admin/products` - Crear producto
- `PUT /api/v1/admin/products/{id}` - Actualizar producto
- `DELETE /api/v1/admin/products/{id}` - Eliminar producto

### Admin - Usuarios (Solo Admin)
- `POST /api/v1/admin/users` - Crear usuario
- `GET /api/v1/admin/users` - Listar usuarios
- `GET /api/v1/admin/users/{id}` - Obtener usuario
- `PUT /api/v1/admin/users/{id}` - Actualizar usuario
- `DELETE /api/v1/admin/users/{id}` - Eliminar usuario

## Información de la API

- **Titel**: Product Purchasing System API
- **Versión**: 1.0
- **Descripción**: API RESTful para el sistema de compra de productos
- **Organización**: Cesde
- **URL**: https://cesde.edu.co

## Esquema de Seguridad

- **Tipo**: Bearer Token (JWT)
- **Formato**: `Authorization: Bearer {token}`
- **Requerido para**: Endpoints autenticados y de administrador

## Dependencias Utilizadas

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.8</version>
</dependency>
```

## Notas Importantes

1. **Todos los controladores** han sido documentados siguiendo estándares OpenAPI 3.0
2. **Códigos de respuesta** están marcados correctamente para cada operación
3. **Esquemas de datos** están vinculados a los DTOs del proyecto
4. **Autenticación** está documentada con Bearer Token JWT
5. **Operaciones administrativas** están marcadas con @SecurityRequirement

## Pruebas

Puedes probar la API directamente desde Swagger UI:
1. Accede a `http://localhost:8080/swagger-ui.html`
2. Ejecuta operaciones públicas primero (productos, categorías)
3. Obtén un token usando `/api/v1/auth/login` o `/api/v1/auth/guest-session`
4. En el botón "Authorize" en la esquina superior derecha
5. Ingresa: `Bearer {token}`
6. Prueba operaciones autenticadas

## Mantenimiento

Para agregar nuevas operaciones:
1. Usa `@Tag` a nivel de controlador
2. Usa `@Operation` en cada método
3. Documenta respuestas con `@ApiResponses`
4. Usa `@Parameter` para cada parámetro
5. Vincula esquemas con `@Schema` o `@ArraySchema`

