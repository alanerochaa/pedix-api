package com.pedix.api.controller.api;

import com.pedix.api.dto.MensagemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
@Tag(
        name = "Home da API",
        description = "Endpoint inicial da API Pedix, responsável por disponibilizar links de navegação, status da aplicação e informações de saúde do serviço."
)
public class ApiHomeController {

    @Operation(
            summary = "Página inicial da API",
            description = "Retorna os principais links de navegação da API Pedix utilizando HATEOAS."
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<MensagemResponse> apiRoot() {

        MensagemResponse mensagem = new MensagemResponse(
                "Bem-vinda à API Pedix. Utilize os links disponíveis para navegar pelos recursos."
        );

        return EntityModel.of(
                mensagem,

                // SELF
                linkTo(methodOn(ApiHomeController.class).apiRoot()).withSelfRel(),

                // LINKS PRINCIPAIS
                linkTo(methodOn(ApiHomeController.class).home()).withRel("home"),
                linkTo(methodOn(ApiHomeController.class).health()).withRel("health"),

                // MÓDULOS
                linkTo(methodOn(PedidoController.class).listarTodos()).withRel("pedidos"),
                linkTo(methodOn(ItemCardapioController.class).listar(null, null)).withRel("cardapio"),
                linkTo(methodOn(PedidoItemController.class).listarTodos()).withRel("itens-pedido"),

                // SWAGGER
                Link.of("/swagger-ui.html").withRel("swagger-ui")
        );
    }

    @Operation(
            summary = "Status da API",
            description = "Retorna informações sobre o funcionamento da API e links para os principais módulos do sistema."
    )
    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<MensagemResponse> home() {

        MensagemResponse mensagem = new MensagemResponse(
                "API Pedix está operacional e pronta para consumo."
        );

        return EntityModel.of(
                mensagem,

                // SELF
                linkTo(methodOn(ApiHomeController.class).home()).withSelfRel(),

                // LINKS PRINCIPAIS
                linkTo(methodOn(PedidoController.class).listarTodos()).withRel("pedidos"),
                linkTo(methodOn(ItemCardapioController.class).listar(null, null)).withRel("cardapio"),
                linkTo(methodOn(PedidoItemController.class).listarTodos()).withRel("itens-pedido"),

                // HEALTH
                linkTo(methodOn(ApiHomeController.class).health()).withRel("health"),

                // SWAGGER
                Link.of("/swagger-ui.html").withRel("swagger-ui")
        );
    }

    @Operation(
            summary = "Health Check da API",
            description = "Verifica o status operacional da API Pedix, retornando informações básicas de disponibilidade do serviço."
    )
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> health() {

        return Map.of(
                "status", "UP",
                "service", "pedix-api-java",
                "environment", "cloud-ready",
                "timestamp", LocalDateTime.now()
        );
    }
}