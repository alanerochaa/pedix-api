package com.pedix.api.controller.api;

import com.pedix.api.dto.PedidoItemRequestDTO;
import com.pedix.api.dto.PedidoItemResponseDTO;
import com.pedix.api.service.PedidoItemService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Hidden
@RestController
@RequestMapping("/api/pedido-item")
@RequiredArgsConstructor
@Tag(
        name = "Itens do Pedido",
        description = """
                Controla os itens individuais vinculados aos pedidos do restaurante.
                Permite criar, listar, buscar, atualizar e remover itens associados aos pedidos.
                """
)
public class PedidoItemController {

    private final PedidoItemService service;

    @Operation(
            summary = "Listar todos os itens de pedido",
            description = "Retorna todos os itens vinculados aos pedidos cadastrados no sistema."
    )
    @GetMapping
    public ResponseEntity<List<EntityModel<PedidoItemResponseDTO>>> listarTodos() {

        List<EntityModel<PedidoItemResponseDTO>> resposta = service.listarTodosDTO().stream()
                .map(dto -> EntityModel.of(dto))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @Operation(
            summary = "Buscar item de pedido por ID",
            description = "Consulta um item específico de pedido utilizando seu identificador único."
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoItemResponseDTO>> buscarPorId(@PathVariable Long id) {

        PedidoItemResponseDTO dto = service.buscarDTOPorId(id);

        return ResponseEntity.ok(EntityModel.of(dto));
    }

    @Operation(
            summary = "Criar novo item de pedido",
            description = "Adiciona um novo item a um pedido existente no sistema."
    )
    @PostMapping
    public ResponseEntity<PedidoItemResponseDTO> criar(
            @Valid @RequestBody PedidoItemRequestDTO dto
    ) {

        PedidoItemResponseDTO resp = service.criar(dto);

        return ResponseEntity.ok(resp);
    }

    @Operation(
            summary = "Atualizar item de pedido",
            description = "Atualiza as informações de um item já vinculado a um pedido."
    )
    @PutMapping("/{id}")
    public ResponseEntity<PedidoItemResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoItemRequestDTO dto
    ) {

        PedidoItemResponseDTO atualizado = service.atualizar(id, dto);

        return ResponseEntity.ok(atualizado);
    }

    @Operation(
            summary = "Remover item de pedido",
            description = "Remove um item vinculado a um pedido utilizando o identificador informado."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        service.deletar(id);

        return ResponseEntity.noContent().build();
    }
}