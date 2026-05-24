package com.pedix.api.controller.api;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.dto.PedidoResponseDTO;
import com.pedix.api.service.PedidoService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Hidden
@RestController
@RequestMapping("/api/pedido")
@RequiredArgsConstructor
@Tag(
        name = "Pedidos",
        description = """
                Controla os pedidos vinculados às comandas do restaurante.
                Permite criar pedidos, listar por comanda, listar todos, buscar por ID,
                atualizar status e remover registros operacionais.
                """
)
public class PedidoController {

    private final PedidoService service;

    @Operation(
            summary = "Listar todos os pedidos",
            description = "Retorna todos os pedidos cadastrados no sistema, com links HATEOAS para navegação entre os recursos."
    )
    @GetMapping
    public ResponseEntity<List<EntityModel<PedidoResponseDTO>>> listarTodos() {
        List<EntityModel<PedidoResponseDTO>> resposta = service.listarTodos().stream()
                .map(service::toResponse)
                .map(dto -> EntityModel.of(
                        dto,
                        linkTo(methodOn(PedidoController.class).obter(dto.getId())).withSelfRel(),
                        linkTo(methodOn(PedidoController.class).listarTodos()).withRel("todos_pedidos")
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @Operation(
            summary = "Buscar pedido por ID",
            description = "Consulta um pedido específico utilizando seu identificador único."
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoResponseDTO>> obter(@PathVariable Long id) {
        Pedido pedido = service.buscarPorId(id);
        PedidoResponseDTO dto = service.toResponse(pedido);

        EntityModel<PedidoResponseDTO> model = EntityModel.of(
                dto,
                linkTo(methodOn(PedidoController.class).obter(id)).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listarTodos()).withRel("todos_pedidos")
        );

        return ResponseEntity.ok(model);
    }
}