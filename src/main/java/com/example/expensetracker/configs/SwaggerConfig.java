package com.example.expensetracker.configs;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "Bearer",
        in = SecuritySchemeIn.HEADER
)
@Configuration
public class SwaggerConfig {


    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("All")
                .pathsToMatch("/idn/**", "/auth/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                                .title("All APIs")
                                .description("This is a list of APIs")
                                .contact(new Contact()
                                        .url("http://localhost:8080").name("expense tracker application")))
                        .addSecurityItem(new SecurityRequirement().addList("Authorization"))
                ).build();
    }
}
