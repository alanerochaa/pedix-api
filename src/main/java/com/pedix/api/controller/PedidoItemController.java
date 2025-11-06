package com.pedix.api.controller;

import com.pedix.api.dto.PedidoItemRequestDTO;
import com.pedix.api.dto.PedidoItemResponseDTO;
import com.pedix.api.service.PedidoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/pedido-item")
@RequiredArgsConstructor
@Tag(
        name = "Item de Pedido",
        description = """
        Controla os **itens individuais de pedidos**.
        Permite **criar**, **listar**, **buscar por ID**, **atualizar** e **remover** itens.
        """
)
public class PedidoItemController {

    private final PedidoItemService service;

    @Operation(summary = "Listar todos os itens de pedido")
    @GetMapping
    public ResponseEntity<List<EntityModel<PedidoItemResponseDTO>>> listarTodos() {
        List<EntityModel<PedidoItemResponseDTO>> resposta = service.listarTodosDTO().stream()
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(PedidoItemController.class).buscarPorId(dto.getId())).withSelfRel(),
                        linkTo(methodOn(PedidoItemController.class).listarTodos()).withRel("todos_itens"),
                        linkTo(methodOn(PedidoController.class).obter(dto.getPedidoId())).withRel("pedido")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @Operation(summary = "Buscar item de pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoItemResponseDTO>> buscarPorId(@PathVariable Long id) {
        PedidoItemResponseDTO dto = service.buscarDTOPorId(id);

        EntityModel<PedidoItemResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(PedidoItemController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(PedidoItemController.class).listarTodos()).withRel("todos_itens"),
                linkTo(methodOn(PedidoController.class).obter(dto.getPedidoId())).withRel("pedido"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Criar novo item de pedido")
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(
            @Valid @RequestBody PedidoItemRequestDTO dto,
            UriComponentsBuilder uriBuilder) {

        PedidoItemResponseDTO resp = service.criar(dto);
        URI location = uriBuilder.path("/api/pedido-item/{id}")
                .buildAndExpand(resp.getId())
                .toUri();

        Map<String, Object> body = Map.of(
                "mensagem", "Item de pedido criado com sucesso!",
                "item", resp,
                "_links", Map.of(
                        "self", linkTo(methodOn(PedidoItemController.class).buscarPorId(resp.getId())).toUri(),
                        "todos_itens", linkTo(methodOn(PedidoItemController.class).listarTodos()).toUri(),
                        "pedido", linkTo(methodOn(PedidoController.class).obter(resp.getPedidoId())).toUri()
                )
        );

        return ResponseEntity.created(location).body(body);
    }

    @Operation(summary = "Atualizar item de pedido")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoItemRequestDTO dto) {

        PedidoItemResponseDTO atualizado = service.atualizar(id, dto);

        Map<String, Object> body = Map.of(
                "mensagem", "Item de pedido atualizado com sucesso!",
                "item", atualizado,
                "_links", Map.of(
                        "self", linkTo(methodOn(PedidoItemController.class).buscarPorId(atualizado.getId())).toUri(),
                        "todos_itens", linkTo(methodOn(PedidoItemController.class).listarTodos()).toUri(),
                        "pedido", linkTo(methodOn(PedidoController.class).obter(atualizado.getPedidoId())).toUri()
                )
        );

        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Deletar item de pedido")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable Long id) {
        Long pedidoId = service.deletar(id);

        Map<String, Object> body = Map.of(
                "mensagem", "Item de pedido removido com sucesso!",
                "status", HttpStatus.OK.value(),
                "_links", Map.of(
                        "pedido", linkTo(methodOn(PedidoController.class).obter(pedidoId)).toUri(),
                        "todos_itens", linkTo(methodOn(PedidoItemController.class).listarTodos()).toUri()
                ),
                "timestamp", java.time.LocalDateTime.now()
        );

        return ResponseEntity.ok(body);
    }
}
