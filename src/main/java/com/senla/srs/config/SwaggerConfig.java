package com.senla.srs.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private static final String DESCRIPTION = "This software was written as part of the final assignment for Java " +
            "development courses from SENLA. The main goal of creating this application was the practical application " +
            "of the knowledge I have accumulated about creating applications using the Spring framework, " +
            "Hibernate and relational databases.";

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "scheme";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                                .name("Authorization"))
                )
                .info(new Info()
                        .title("Scooter Rental Service Swagger API")
                        .description(DESCRIPTION)
                        .version("1.0.0")
                        .contact(new Contact()
                                .email("mehailpost@gmail.com")
                                .url("https://github.com/mehail/ScooterRentalService")
                                .name("Mihail Artyugin"))
                );
    }
}
