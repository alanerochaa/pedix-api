package com.pedix.api.controller;

import com.pedix.api.dto.MensagemResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class HomeController {

    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<MensagemResponse> home() {
        MensagemResponse mensagem = new MensagemResponse(
                "API Pedix está rodando! Acesse o Swagger UI ou as rotas principais."
        );

        EntityModel<MensagemResponse> model = EntityModel.of(mensagem,
                linkTo(methodOn(HomeController.class).home()).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listarTodos()).withRel("pedidos"),
                linkTo(methodOn(ItemCardapioController.class).listar(null)).withRel("cardapio"),
                Link.of("/swagger-ui/index.html").withRel("swagger-ui")
        );

        return model;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String paginaInicial() {
        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <meta charset="UTF-8">
                  <title>Pedix API</title>
                  <style>
                    body {
                      font-family: Arial, sans-serif;
                      text-align: center;
                      margin-top: 60px;
                      background-color: #f4f6f8;
                      color: #333;
                    }
                    a {
                      color: #007bff;
                      text-decoration: none;
                      font-weight: bold;
                    }
                    a:hover {
                      text-decoration: underline;
                    }
                  </style>
                </head>
                <body>
                  <h1> API Pedix está rodando!</h1>
                  <p>Acesse o <a href="/swagger-ui/index.html">Swagger UI</a> para testar os endpoints.</p>
                  <p>Ou consulte as rotas JSON em <a href="/home">/home</a>.</p>
                </body>
                </html>
                """;
    }
}
