package com.clinicbook.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String BEARER_SCHEME = "bearerAuth";

    @Bean
    public OpenAPI clinicBookOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Clinic Book API")
                        .version("v1")
                        .description("""
                                Role-based medical appointment scheduling API.

                                Every endpoint except `/api/auth/signup` and `/api/auth/login`
                                requires a bearer token. Log in through `/api/auth/login`, copy
                                the returned token and paste it into **Authorize** to try the
                                protected operations from this page.""")
                        .contact(new Contact().name("Miguel Mora").url("https://github.com/migueldevplusplus")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
                .components(new Components().addSecuritySchemes(BEARER_SCHEME,
                        new SecurityScheme()
                                .name(BEARER_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
