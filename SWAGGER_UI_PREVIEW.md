# 👀 Vista Previa: Qué Verás en Swagger UI

## En tu navegador: `http://localhost:8080/swagger-ui.html`

```
╔══════════════════════════════════════════════════════════════════════════╗
║                      SWAGGER UI - Vista Completa                         ║
╠══════════════════════════════════════════════════════════════════════════╣
║                                                                          ║
║   Product Purchasing System API v1.0                    [Authorize] 🔐  ║
║   API RESTful para el sistema de compra de productos                    ║
║   Contact: Cesde (https://cesde.edu.co)                [▼ Servers ▼]  ║
║                                                                          ║
╠══════════════════════════════════════════════════════════════════════════╣
║ 📦 Autenticación                                                         ║
║ ├─ POST   /api/v1/auth/guest-session         Crear sesión de invitado  ║
║ ├─ POST   /api/v1/auth/register              Registrar nuevo usuario   ║
║ ├─ POST   /api/v1/auth/login                 Iniciar sesión           ║
║ ├─ GET    /api/v1/auth/me                    Obtener usuario actual    ║
║ └─ POST   /api/v1/auth/logout                Cerrar sesión             ║
║                                                                          ║
║ 📦 Productos                                                             ║
║ ├─ GET    /api/v1/products                   Listar productos          ║
║ └─ GET    /api/v1/products/{id}              Obtener producto por ID   ║
║                                                                          ║
║ 📦 Categorías                                                            ║
║ ├─ GET    /api/v1/categories                 Listar categorías         ║
║ ├─ GET    /api/v1/categories/tree            Obtener árbol jerárquico  ║
║ ├─ GET    /api/v1/categories/{id}            Obtener categoría por ID  ║
║ └─ GET    /api/v1/categories/{id}/subcategories  Listar subcategorías ║
║                                                                          ║
║ 📦 Carrito de Compras                                                    ║
║ ├─ GET    /api/v1/cart/me                    Obtener carrito actual    ║
║ ├─ POST   /api/v1/cart/items                 Agregar producto          ║
║ ├─ PATCH  /api/v1/cart/items/{productId}     Actualizar cantidad       ║
║ ├─ DELETE /api/v1/cart/items/{productId}     Eliminar producto         ║
║ ├─ DELETE /api/v1/cart/items                 Limpiar carrito           ║
║ └─ POST   /api/v1/cart/merge                 Fusionar carrito invitado ║
║                                                                          ║
║ 📦 Órdenes de Compra                                                     ║
║ ├─ POST   /api/v1/orders/checkout            Procesar compra           ║
║ ├─ GET    /api/v1/orders/me                  Listar mis órdenes        ║
║ └─ GET    /api/v1/orders/{id}                Obtener orden específica   ║
║                                                                          ║
║ 📦 Direcciones de Usuario                                                ║
║ ├─ GET    /api/v1/users/me/addresses         Listar mis direcciones    ║
║ ├─ GET    /api/v1/users/me/addresses/{id}    Obtener dirección         ║
║ ├─ POST   /api/v1/users/me/addresses         Agregar dirección         ║
║ ├─ PUT    /api/v1/users/me/addresses/{id}    Actualizar dirección      ║
║ ├─ PATCH  /api/v1/users/me/addresses/{id}/default  Dirección por defecto ║
║ └─ DELETE /api/v1/users/me/addresses/{id}    Eliminar dirección        ║
║                                                                          ║
║ 📦 Perfil de Usuario                                                     ║
║ ├─ PUT    /api/v1/users/me                   Actualizar perfil         ║
║ └─ PUT    /api/v1/users/me/password          Cambiar contraseña        ║
║                                                                          ║
║ 📦 Admin - Productos  🔐                                                 ║
║ ├─ POST   /api/v1/admin/products             Crear producto            ║
║ ├─ PUT    /api/v1/admin/products/{id}        Actualizar producto       ║
║ └─ DELETE /api/v1/admin/products/{id}        Eliminar producto         ║
║                                                                          ║
║ 📦 Admin - Usuarios  🔐                                                  ║
║ ├─ POST   /api/v1/admin/users                Crear usuario             ║
║ ├─ GET    /api/v1/admin/users                Listar usuarios           ║
║ ├─ GET    /api/v1/admin/users/{id}           Obtener usuario           ║
║ ├─ PUT    /api/v1/admin/users/{id}           Actualizar usuario        ║
║ └─ DELETE /api/v1/admin/users/{id}           Eliminar usuario          ║
║                                                                          ║
╚══════════════════════════════════════════════════════════════════════════╝
```

---

## Cuando haces click en un endpoint:

```
╔═══════════════════════════════════════════════════════════════════════════╗
║ POST /api/v1/products/checkout                                           ║
║                                                                           ║
║ Procesar compra                                                          ║
║ Realiza el checkout del carrito y crea una nueva orden                  ║
║                                                                           ║
║ ┌─────────────────────────────────────────────────────────────────────┐  ║
║ │ PARAMETERS                                                          │  ║
║ ├─────────────────────────────────────────────────────────────────────┤  ║
║ │ • authorization (header, required)                                  │  ║
║ │   Type: string                                                      │  ║
║ │   Description: Bearer token                                         │  ║
║ │   Example: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...           │  ║
║ │                                                                     │  ║
║ │ • body (CheckoutRequest, required)                                  │  ║
║ │   {                                                                 │  ║
║ │     \"addressId\": 1,        ← dirección de envío                   │  ║
║ │     \"paymentMethod\": \"CREDIT_CARD\"    ← método de pago          │  ║
║ │   }                                                                 │  ║
║ └─────────────────────────────────────────────────────────────────────┘  ║
║                                                                           ║
║ ┌─────────────────────────────────────────────────────────────────────┐  ║
║ │ RESPONSES                                                           │  ║
║ ├─────────────────────────────────────────────────────────────────────┤  ║
║ │ 201 ✓ Orden creada exitosamente                                     │  ║
║ │     MediaType: application/json                                     │  ║
║ │     {                                                               │  ║
║ │       \"id\": 1,                                                     │  ║
║ │       \"orderNumber\": \"ORD-2024-001\",                            │  ║
║ │       \"totalAmount\": 2599.98,                                     │  ║
║ │       \"status\": \"PENDING\",                                      │  ║
║ │       \"items\": [...],                                             │  ║
║ │       \"createdAt\": \"2024-04-29T10:30:00Z\"                       │  ║
║ │     }                                                               │  ║
║ │                                                                     │  ║
║ │ 400 ✗ Datos inválidos o carrito vacío                               │  ║
║ │ 401 ✗ No autenticado                                                │  ║
║ │ 500 ✗ Error interno del servidor                                    │  ║
║ └─────────────────────────────────────────────────────────────────────┘  ║
║                                                                           ║
║ [Try it out]  └─ Botón para hacer una prueba real                       ║
║                                                                           ║
╚═══════════════════════════════════════════════════════════════════════════╝
```

---

## Cuando haces click en \"Try it out\":

```
╔═══════════════════════════════════════════════════════════════════════════╗
║ POST /api/v1/orders/checkout                                             ║
║                                                                           ║
║ [▼ Try it out] [Cancel]                                                  ║
║                                                                           ║
║ ┌─ Parameters ──────────────────────────────────────────────────────┐   ║
║ │ authorization* (header)                                          │   ║
║ │ [_____________________________________]                          │   ║
║ │  Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...                   │   ║
║ └────────────────────────────────────────────────────────────────────┘   ║
║                                                                           ║
║ ┌─ Request body ────────────────────────────────────────────────────┐   ║
║ │ {                                                                │   ║
║ │   \"addressId\": 1,                                               │   ║
║ │   \"paymentMethod\": \"CREDIT_CARD\"                              │   ║
║ │ }                                                                │   ║
║ └────────────────────────────────────────────────────────────────────┘   ║
║                                                                           ║
║ [Execute]            ← Botón para enviar la petición                    ║
║                                                                           ║
╚═══════════════════════════════════════════════════════════════════════════╝
```

---

## Después de hacer \"Execute\":

```
╔═══════════════════════════════════════════════════════════════════════════╗
║ RESPONSES                                                                ║
║                                                                           ║
║ ┌─ Request URL ─────────────────────────────────────────────────────┐   ║
║ │ POST http://localhost:8080/api/v1/orders/checkout                 │   ║
║ └───────────────────────────────────────────────────────────────────┘   ║
║                                                                           ║
║ ┌─ Response Headers ────────────────────────────────────────────────┐   ║
║ │ content-type: application/json                                   │   ║
║ │ content-length: 342                                              │   ║
║ │ status: 201                                                      │   ║
║ │ http-status-code: 201                                            │   ║
║ └───────────────────────────────────────────────────────────────────┘   ║
║                                                                           ║
║ ┌─ Response Body (201) 201 ✓ ─────────────────────────────────────┐   ║
║ │ {                                                                │   ║
║ │   \"id\": 1,                                                     │   ║
║ │   \"orderNumber\": \"ORD-2024-001\",                             │   ║
║ │   \"totalAmount\": 2599.98,                                      │   ║
║ │   \"status\": \"PENDING\",                                       │   ║
║ │   \"address\": {                                                 │   ║
║ │     \"street\": \"Calle Principal 123\",                         │   ║
║ │     \"city\": \"Bogotá\"                                          │   ║
║ │   },                                                             │   ║
║ │   \"items\": [                                                   │   ║
║ │     {                                                            │   ║
║ │       \"productId\": 1,                                          │   ║
║ │       \"productName\": \"Laptop XPS 15\",                        │   ║
║ │       \"quantity\": 2,                                           │   ║
║ │       \"unitPrice\": 1299.99                                     │   ║
║ │     }                                                            │   ║
║ │   ],                                                             │   ║
║ │   \"createdAt\": \"2024-04-29T10:30:00.123Z\"                    │   ║
║ │ }                                                                │   ║
║ └───────────────────────────────────────────────────────────────────┘   ║
║                                                                           ║
║ ┌─ cURL ────────────────────────────────────────────────────────────┐   ║
║ │ curl -X POST \"http://localhost:8080/api/v1/orders/checkout\" \\     │   ║
║ │   -H \"accept: application/json\" \\                               │   ║
║ │   -H \"Content-Type: application/json\" \\                        │   ║
║ │   -H \"Authorization: Bearer eyJhbGc...\" \\                      │   ║
║ │   -d \"{\\\"addressId\\\":1,\\\"paymentMethod\\\":\\\"CREDIT_CARD\\\"}\"    │   ║
║ └───────────────────────────────────────────────────────────────────┘   ║
║                                                                           ║
╚═══════════════════════════════════════════════════════════════════════════╝
```

---

## El Botón \"Authorize\" (para endpoints protegidos):

```
┌─────────────────────────────────────────────────────┐
│ 🔐 Authorization                                    │
├─────────────────────────────────────────────────────┤
│                                                     │
│ bearerAuth - Type: API Key                          │
│ ┌──────────────────────────────────────────────┐   │
│ │ bearerAuth (HTTP, Bearer)                    │   │
│ │ Token*                                        │   │
│ │ [____________________________________]        │   │
│ │  Ingresa: Bearer eyJhbGciOiJIUzI1...         │   │
│ │                                               │   │
│ │ [Authorize]  [Cancel]                        │   │
│ └──────────────────────────────────────────────┘   │
│                                                     │
└─────────────────────────────────────────────────────┘
```

El token se incluirá automáticamente en todos tus requests posteriores.

---

## Endpoints \"Admin\" tienen este icono:

```
🔐 POST /api/v1/admin/products         Crear producto

El candado 🔐 indica que:
- Requiere autenticación JWT
- Requiere permisos de administrador
```

---

## Tipos de Colores por Método HTTP:

```
GET    → Verde    □  Lectura de datos
POST   → Azul     □  Crear recursos
PUT    → Naranja  □  Actualizar completo
PATCH  → Amarillo □  Actualizar parcial
DELETE → Rojo     □  Eliminar
```

---

## Secciones que Verás para Cada Endpoint:

```
┌─ TÍTULO Y DESCRIPCIÓN ────────────────────────────┐
│ Procesar compra                                   │
│ Realiza el checkout del carrito...                │
└───────────────────────────────────────────────────┘

┌─ PARÁMETROS ──────────────────────────────────────┐
│ • Path parameters                                 │
│ • Query parameters                                │
│ • Headers                                         │
│ • Request body                                    │
└───────────────────────────────────────────────────┘

┌─ CÓDIGOS DE RESPUESTA ────────────────────────────┐
│ 201 ✓ Orden creada                                │
│ 400 ✗ Datos inválidos                             │
│ 401 ✗ No autenticado                              │
│ 500 ✗ Error servidor                              │
└───────────────────────────────────────────────────┘

┌─ ESQUEMAS JSON ───────────────────────────────────┐
│ Estructura de los DTOs                            │
│ Tipos de datos                                    │
│ Campos requeridos                                 │
└───────────────────────────────────────────────────┘
```

---

## Filtro y Búsqueda en Swagger UI:

```
┌─ Filter by tag name ──────────────┐
│ [search...........................] │
│                                   │
│ Encuentra rápidamente lo que buscas│
└───────────────────────────────────┘
```

---

## Descarga OpenAPI:

En la parte superior verás:

```
▼ Explore
  http://localhost:8080/v3/api-docs
```

Aquí puedes obtener:
- **JSON** - Para generar clientes automáticamente
- **YAML** - Formato alternativo
- **HTML** - Para documentación impresa

---

## Ejemplo Real: Buscar tu primer endpoint

1. **Abre:** `http://localhost:8080/swagger-ui.html`
2. **Busca:** \"login\" en el filtro
3. **Verás:**
   ```
   POST /api/v1/auth/login
   Iniciar sesión
   ```
4. **Click en él** → Se expande mostrando parámetros
5. **Click en \"Try it out\"** → Completa los datos
6. **Click en \"Execute\"** → Ves la respuesta

¡Y listo! 🎉

---

**¡Tu documentación es así de linda y funcional! 😊**

