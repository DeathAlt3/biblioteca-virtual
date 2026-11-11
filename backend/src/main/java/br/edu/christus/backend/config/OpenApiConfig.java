package br.edu.christus.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Informações gerais exibidas no topo do Swagger UI (/swagger-ui.html). */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bibliotecaOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Biblioteca Pública Virtual - API REST")
                .version("1.0.0")
                .description("Backend da biblioteca virtual de uma escola de ensino médio: "
                        + "acervo digital (livros, apostilas, artigos e resumos), autores, disciplinas, "
                        + "avaliações dos estudantes e acompanhamento do progresso de leitura. "
                        + "Projeto NP2 - Alan Sávio e João Victor."));
    }
}
