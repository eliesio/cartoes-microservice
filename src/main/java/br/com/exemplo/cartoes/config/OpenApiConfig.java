package br.com.exemplo.cartoes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cartoesOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Microservico de Cartoes")
                        .description("API para criacao de cartoes fisicos e online")
                        .version("v1"));
    }
}