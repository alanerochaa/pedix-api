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
        description = "Endpoint inicial da API Java do Pedix, responsável por disponibilizar links de navegação, status da aplicação e informações de saúde do serviço."
)
public class ApiHomeController {

    @Operation(
            summary = "Página inicial da API Java",
            description = "Retorna os principais links de navegação da API Java do Pedix utilizando HATEOAS."
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<MensagemResponse> apiRoot() {

        MensagemResponse mensagem = new MensagemResponse(
                "Bem-vinda à API Java do Pedix. Este serviço atua como camada secundária de suporte e gestão, sendo consumido pelo aplicativo mobile e integrado ao ecossistema Pedix."
        );

        return EntityModel.of(
                mensagem,

                linkTo(methodOn(ApiHomeController.class).apiRoot()).withSelfRel(),

                linkTo(methodOn(ApiHomeController.class).home()).withRel("home"),
                linkTo(methodOn(ApiHomeController.class).health()).withRel("health"),

                linkTo(methodOn(PedidoController.class).listarTodos()).withRel("pedidos-apoio"),
                linkTo(methodOn(ItemCardapioController.class).listar(null, null)).withRel("cardapio"),
                linkTo(methodOn(PedidoItemController.class).listarTodos()).withRel("itens-pedido-apoio"),

                Link.of("/api/categorias-cardapio").withRel("categorias-cardapio"),
                Link.of("/api/avaliacoes").withRel("avaliacoes"),
                Link.of("/api/historicos-pedidos").withRel("historicos-pedidos"),
                Link.of("/api/relatorios").withRel("relatorios"),
                Link.of("/swagger-ui.html").withRel("swagger-ui")
        );
    }

    @Operation(
            summary = "Status da API Java",
            description = "Retorna informações sobre o funcionamento da API Java e links para os principais módulos de suporte do sistema."
    )
    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<MensagemResponse> home() {

        MensagemResponse mensagem = new MensagemResponse(
                "API Java do Pedix operacional. Serviço secundário de suporte e gestão para cardápio, avaliações, histórico, relatórios e integração mobile."
        );

        return EntityModel.of(
                mensagem,

                linkTo(methodOn(ApiHomeController.class).home()).withSelfRel(),

                linkTo(methodOn(ItemCardapioController.class).listar(null, null)).withRel("cardapio"),
                Link.of("/api/categorias-cardapio").withRel("categorias-cardapio"),
                Link.of("/api/avaliacoes").withRel("avaliacoes"),
                Link.of("/api/historicos-pedidos").withRel("historicos-pedidos"),
                Link.of("/api/relatorios").withRel("relatorios"),

                linkTo(methodOn(PedidoController.class).listarTodos()).withRel("pedidos-apoio"),
                linkTo(methodOn(PedidoItemController.class).listarTodos()).withRel("itens-pedido-apoio"),

                linkTo(methodOn(ApiHomeController.class).health()).withRel("health"),

                Link.of("/swagger-ui.html").withRel("swagger-ui")
        );
    }

    @Operation(
            summary = "Health Check da API",
            description = "Verifica o status operacional da API Java do Pedix, retornando informações básicas de disponibilidade do serviço."
    )
    @GetMapping(value = "/health", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> health() {

        return Map.of(
                "status", "UP",
                "service", "pedix-api-java",
                "role", "secondary-support-management-api",
                "consumedBy", "pedix-mobile",
                "mainOperationalFlow", "pedix-csharp-api",
                "environment", "cloud-ready",
                "timestamp", LocalDateTime.now()
        );
    }
}