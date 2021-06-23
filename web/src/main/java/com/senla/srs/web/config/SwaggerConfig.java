package com.senla.srs.web.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String DESCRIPTION = "This software was written as part of the final assignment for Java " +
            "development courses from SENLA. The main goal of creating this application was the practical application " +
            "of the knowledge I have accumulated about creating applications using the Spring framework, " +
            "Hibernate and relational databases.";
    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${swagger.version}")
    private String version;
    @Value("${swagger.email}")
    private String email;
    @Value("${swagger.url}")
    private String url;
    @Value("${swagger.name}")
    private String name;

    @Bean
    public OpenAPI customOpenAPI() {
        final var securitySchemeName = "JWT token";

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.APIKEY)
                                                .in(SecurityScheme.In.HEADER)
                                                .name(authorizationHeader))
                )
                .info(new Info()
                        .title("Scooter Rental Service Swagger API")
                        .description(DESCRIPTION)
                        .version(version)
                        .contact(new Contact()
                                .email(email)
                                .url(url)
                                .name(name))
                );
    }

}
