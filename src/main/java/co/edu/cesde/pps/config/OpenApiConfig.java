package co.edu.cesde.pps.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración centralizada de OpenAPI/Swagger para la API.
 * Define esquemas de seguridad, información de la API y servidores.
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Product Purchasing System API",
        version = "1.0",
        description = "API RESTful para el sistema de compra de productos",
        contact = @Contact(
            name = "Cesde",
            url = "https://cesde.edu.co"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Servidor de desarrollo"
        )
    },
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

