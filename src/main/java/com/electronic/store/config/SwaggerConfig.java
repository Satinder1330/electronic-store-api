package com.electronic.store.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOPenApi(){
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new Info().title("Electronics Store Backend:APIs")
                .description("This is a SpringBoot Backend Project")
                .version("1.0.0")
                .contact(new Contact().email("abc@gmail.com")
                        .url("www.facebook.com/abc")
                        .name("Satinder Singh")
                        ).license(new License().name("License for api").url("abc@gmail.com")));
        // Jwt Security
        openAPI.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));
        return openAPI;
    }
}
