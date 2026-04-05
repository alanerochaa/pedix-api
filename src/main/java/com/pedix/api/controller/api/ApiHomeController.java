package com.pedix.api.controller.api;

import com.pedix.api.dto.MensagemResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api")
public class ApiHomeController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<MensagemResponse> apiRoot() {
        MensagemResponse mensagem = new MensagemResponse(
                "Bem-vinda à API Pedix. Utilize os links disponíveis para navegar."
        );

        return EntityModel.of(
                mensagem,
                linkTo(methodOn(ApiHomeController.class).apiRoot()).withSelfRel(),
                linkTo(methodOn(ApiHomeController.class).home()).withRel("status"),
                Link.of("/swagger-ui/index.html").withRel("swagger-ui")
        );
    }

    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public EntityModel<MensagemResponse> home() {
        MensagemResponse mensagem = new MensagemResponse(
                "API Pedix está operacional. Acesse os recursos disponíveis."
        );

        return EntityModel.of(
                mensagem,
                linkTo(methodOn(ApiHomeController.class).home()).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listarTodos()).withRel("pedidos"),
                linkTo(methodOn(ItemCardapioController.class).listar(null, null)).withRel("cardapio"),
                linkTo(methodOn(PedidoItemController.class).listarTodos()).withRel("itens-pedido"),
                Link.of("/swagger-ui/index.html").withRel("swagger-ui")
        );
    }
}