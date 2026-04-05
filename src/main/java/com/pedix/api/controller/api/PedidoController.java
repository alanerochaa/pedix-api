package com.pedix.api.controller.api;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.dto.PedidoResponseDTO;
import com.pedix.api.service.PedidoService;
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

@RestController
@RequestMapping("/api/pedido")
@RequiredArgsConstructor
@Tag(
        name = "Pedido",
        description = """
        Controla os pedidos vinculados às comandas do restaurante.
        Permite criar pedidos, listar por comanda, listar todos, buscar por ID e atualizar status.
        """
)
public class PedidoController {

    private final PedidoService service;

    @Operation(summary = "Listar todos os pedidos")
    @GetMapping
    public ResponseEntity<List<EntityModel<PedidoResponseDTO>>> listarTodos() {
        List<EntityModel<PedidoResponseDTO>> resposta = service.listarTodos().stream()
                .map(service::toResponse)
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(PedidoController.class).obter(dto.getId())).withSelfRel(),
                        linkTo(methodOn(PedidoController.class).listarTodos()).withRel("todos_pedidos")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @Operation(summary = "Buscar pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoResponseDTO>> obter(@PathVariable Long id) {
        Pedido pedido = service.buscarPorId(id);
        PedidoResponseDTO dto = service.toResponse(pedido);

        EntityModel<PedidoResponseDTO> model = EntityModel.of(dto,
                linkTo(methodOn(PedidoController.class).obter(id)).withSelfRel(),
                linkTo(methodOn(PedidoController.class).listarTodos()).withRel("todos_pedidos"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Listar pedidos por comanda")
    @GetMapping("/comanda/{comandaId}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorComanda(@PathVariable Long comandaId) {
        List<PedidoResponseDTO> resposta = service.listarPorComanda(comandaId).stream()
                .map(service::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @Operation(summary = "Criar novo pedido vinculado a uma comanda")
    @PostMapping("/comanda/{comandaId}")
    public ResponseEntity<Map<String, Object>> criar(
            @PathVariable Long comandaId,
            @Valid @RequestBody PedidoDTO dto,
            Authentication authentication,
            UriComponentsBuilder uri) {

        String loginGarcom = authentication != null ? authentication.getName() : "api";

        PedidoResponseDTO resp = service.criarPedido(comandaId, dto, loginGarcom);
        URI location = uri.path("/api/pedido/{id}").buildAndExpand(resp.getId()).toUri();

        Map<String, Object> body = Map.of(
                "mensagem", "Pedido criado com sucesso!",
                "pedido", resp,
                "_links", Map.of(
                        "self", linkTo(methodOn(PedidoController.class).obter(resp.getId())).toUri(),
                        "todos_pedidos", linkTo(methodOn(PedidoController.class).listarTodos()).toUri()
                )
        );

        return ResponseEntity.created(location).body(body);
    }

    @Operation(summary = "Atualizar status do pedido")
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status) {

        PedidoResponseDTO atualizado = service.atualizarStatus(id, status);

        Map<String, Object> body = Map.of(
                "mensagem", "Status do pedido atualizado com sucesso!",
                "pedido", atualizado,
                "_links", Map.of(
                        "self", linkTo(methodOn(PedidoController.class).obter(atualizado.getId())).toUri(),
                        "todos_pedidos", linkTo(methodOn(PedidoController.class).listarTodos()).toUri()
                )
        );

        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Excluir pedido por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> excluir(@PathVariable Long id) {
        service.excluir(id);

        Map<String, Object> body = Map.of(
                "mensagem", "Pedido removido com sucesso!",
                "status", HttpStatus.OK.value(),
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity.ok(body);
    }
}