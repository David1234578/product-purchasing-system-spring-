# 🚀 Guía Rápida - Swagger/OpenAPI

## Inicio Rápido

### 1. **Iniciar la aplicación**

```bash
# Windows PowerShell
cd "C:\Users\juand\Documents\Workspace and Information\Cesde\Back\product-purchasing-system-spring-"
mvn spring-boot:run
```

Verás en la consola:
```
:::::::::::::::::::::::::::::::::::::
: Product Purchasing System Started :
:::::::::::::::::::::::::::::::::::::

Started PpsApplication in X.XXX seconds (JVM running for X.XXX)
```

### 2. **Abrir Swagger UI**

Abre tu navegador y ve a:

```
http://localhost:8080/swagger-ui.html
```

¡Verás una interfaz interactiva donde puedes ver y probar todos los endpoints! 🎉

---

## 📋 Flujo de Prueba Recomendado

### **Paso 1: Crear sesión de invitado (Público)**

```
POST /api/v1/auth/guest-session
```

Respuesta:
```json
{
  "sessionId": "abc123...",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresAt": "2024-04-30T12:00:00Z"
}
```

**Guarda el token** para las próximas peticiones.

### **Paso 2: Explorar productos (Público)**

```
GET /api/v1/products
```

Parámetros opcionales:
- `search` - Buscar por nombre
- `categoryId` - Filtrar por categoría
- `activeOnly` - Solo productos activos (default: true)

### **Paso 3: Obtener categorías (Público)**

```
GET /api/v1/categories
GET /api/v1/categories/tree
```

### **Paso 4: Autorizar con token en Swagger UI**

1. Al lado del título "Product Purchasing System API" verás un botón **"Authorize"** 🔐
2. Haz click
3. En el campo de texto, ingresa:
   ```
   Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9... (tu token)
   ```
4. Haz click en "Authorize"
5. Haz click en "Close"

### **Paso 5: Probrar operaciones autenticadas**

Ahora puedes probar:

```
GET /api/v1/users/me           # Ver tu usuario
GET /api/v1/cart/me            # Ver tu carrito
POST /api/v1/cart/items        # Agregar al carrito
```

---

## 🔐 Autenticación con Token

### Crear cuenta (Registro)

```
POST /api/v1/auth/register
```

**Body (JSON):**
```json
{
  "email": "usuario@example.com",
  "password": "MiPassword123!",
  "firstName": "Juan",
  "lastName": "Pérez"
}
```

### Iniciar sesión

```
POST /api/v1/auth/login
```

**Body (JSON):**
```json
{
  "email": "usuario@example.com",
  "password": "MiPassword123!"
}
```

**Respuesta:**
```json
{
  "sessionId": "sess_xyz...",
  "token": "eyJhbGc...",
  "expiresAt": "2024-04-30T12:00:00Z"
}
```

---

## 📱 Ejemplos de Peticiones

### Agregar producto al carrito

```
POST /api/v1/cart/items
```

**Headers:**
```
Authorization: Bearer {tu_token}
Content-Type: application/json
```

**Body:**
```json
{
  "productId": 1,
  "quantity": 2
}
```

### Crear dirección

```
POST /api/v1/users/me/addresses
```

**Headers:**
```
Authorization: Bearer {tu_token}
Content-Type: application/json
```

**Body:**
```json
{
  "street": "Calle Principal 123",
  "city": "Bogotá",
  "state": "DC",
  "zipCode": "110111",
  "country": "Colombia"
}
```

### Procesar compra (Checkout)

```
POST /api/v1/orders/checkout
```

**Headers:**
```
Authorization: Bearer {tu_token}
```

**Body:**
```json
{
  "addressId": 1,
  "paymentMethod": "CREDIT_CARD"
}
```

---

## 🛠️ Filtros y Búsqueda

### Buscar productos

```
GET /api/v1/products?search=laptop
GET /api/v1/products?search=iphone&activeOnly=true
```

### Filtrar por categoría

```
GET /api/v1/products?categoryId=5
```

### Combinar filtros

```
GET /api/v1/products?search=notebook&categoryId=3&activeOnly=true
```

---

## 👨‍💼 Operaciones Administrativas

### ⚠️ Requiere cuenta con permisos de administrador

Primero obtén un token de administrador, luego:

### Crear usuario (Admin)

```
POST /api/v1/admin/users
```

**Headers:**
```
Authorization: Bearer {admin_token}
```

**Body:**
```json
{
  "email": "newuser@example.com",
  "password": "SecurePass123!",
  "firstName": "Carlos",
  "lastName": "López",
  "role": "USER"
}
```

### Crear producto (Admin)

```
POST /api/v1/admin/products
```

**Body:**
```json
{
  "name": "Laptop XPS 15",
  "description": "Laptop profesional de 15 pulgadas",
  "price": 1299.99,
  "stock": 50,
  "categoryId": 2
}
```

### Listar todos los usuarios (Admin)

```
GET /api/v1/admin/users
```

---

## 🐛 Solución de Problemas

### **Error 401 Unauthorized**
- El token ha expirado
- El token es inválido
- Formato incorrecto: debe ser `Bearer {token}`

**Solución:** Obtén un nuevo token en `/api/v1/auth/login`

### **Error 403 Forbidden**
- Intentaste acceder a operación de admin sin permisos
- Tu usuario no tiene rol de administrador

**Solución:** Usa una cuenta de admin o contacta al administrador

### **Error 404 Not Found**
- El recurso no existe (producto eliminado, orden no encontrada)

**Solución:** Verifica que el ID sea correcto

### **Error 400 Bad Request**
- Los datos enviados son inválidos
- Faltan campos requeridos
- El formato es incorrecto

**Solución:** Revisa el mensaje de error y el schema en Swagger UI

### **Error 500 Internal Server Error**
- Error en el servidor
- Revisa los logs de la aplicación

---

## 📊 Ver Especificación OpenAPI

### Formato JSON
```
http://localhost:8080/v3/api-docs
```

### Formato YAML
```
http://localhost:8080/v3/api-docs.yaml
```

Usa estos para:
- Generar clientes automáticamente
- Documentación externa
- Integración con otras herramientas

---

## 🔗 Rutas principales

| Componente | URL |
|-----------|-----|
| Swagger UI | `http://localhost:8080/swagger-ui.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| OpenAPI YAML | `http://localhost:8080/v3/api-docs.yaml` |
| API Base | `http://localhost:8080/api/v1` |

---

## 💡 Consejos

1. **Usa Swagger UI para probar** - Es mucho más fácil que Postman
2. **Guarda tus tokens** - Para poder seguir probando después
3. **Lee las descripciones** - Cada endpoint tiene descripción completa
4. **Revisa los esquemas** - Entiende qué datos enviar y esperar
5. **Usa "Try it out"** - El botón en cada endpoint para hacer requests

---

## ✨ ¿Qué viene después?

Una vez que tengas Swagger funcionando:

1. ✅ Prueba todos los endpoints públicos (sin autenticación)
2. ✅ Registra una cuenta y prueba endpoints autenticados
3. ✅ Implementa un cliente (Frontend, Mobile, etc.) usando los DTOs del Swagger
4. ✅ Integra la especificación OpenAPI en tu documentación

---

**¡Tu API está completamente documentada! 🎉**

Para más información, consulta:
- `SWAGGER_CONFIGURATION.md` - Detalles de configuración
- `SWAGGER_SUMMARY.md` - Resumen de anotaciones

