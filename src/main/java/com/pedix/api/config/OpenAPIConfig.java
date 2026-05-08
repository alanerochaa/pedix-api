package com.pedix.api.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI pedixOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pedix API – Comandas Inteligentes")
                        .description("""
                                    API do módulo secundário do sistema Pedix, responsável por
                                    gestão de cardápio, categorias, avaliações, histórico de pedidos
                                    e relatórios administrativos.
                                    """)
                        .version("v2.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório do Projeto")
                        .url("https://github.com/alanerochaa/pedix-api"));
    }
}
