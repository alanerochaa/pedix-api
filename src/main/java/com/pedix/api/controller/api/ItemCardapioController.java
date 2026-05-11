package com.pedix.api.controller.api;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.dto.ItemCardapioDTO;
import com.pedix.api.service.ItemCardapioService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/item-cardapio")
@RequiredArgsConstructor
@Tag(
        name = "Cardápio",
        description = """
                Controla os itens do cardápio do restaurante.
                Permite criar, listar, buscar, atualizar e remover pratos,
                bebidas e sobremesas, vinculando cada item a uma categoria.
                """
)
public class ItemCardapioController {

    private final ItemCardapioService service;

    @Operation(
            summary = "Listar itens do cardápio",
            description = "Retorna todos os itens disponíveis no cardápio, permitindo filtros por categoria ou busca textual."
    )
    @GetMapping
    public ResponseEntity<List<EntityModel<ItemCardapioDTO>>> listar(
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) String busca
    ) {
        List<ItemCardapio> itens;

        if (busca != null && !busca.trim().isEmpty()) {
            itens = service.buscarDisponiveisPorNome(busca);
        } else if (categoriaId != null) {
            itens = service.listarPorCategoria(categoriaId);
        } else {
            itens = service.listarDisponiveis();
        }

        List<EntityModel<ItemCardapioDTO>> resposta = itens.stream()
                .map(item -> EntityModel.of(
                        ItemCardapioDTO.fromEntity(item),
                        linkTo(methodOn(ItemCardapioController.class)
                                .buscarPorId(item.getId()))
                                .withSelfRel(),
                        linkTo(methodOn(ItemCardapioController.class)
                                .listar(null, null))
                                .withRel("todos_itens")
                ))
                .toList();

        return ResponseEntity.ok(resposta);
    }

    @Operation(
            summary = "Buscar item por ID",
            description = "Consulta um item específico do cardápio utilizando seu identificador único."
    )
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ItemCardapioDTO>> buscarPorId(@PathVariable Long id) {
        ItemCardapio item = service.buscarPorId(id);

        EntityModel<ItemCardapioDTO> model = EntityModel.of(
                ItemCardapioDTO.fromEntity(item),
                linkTo(methodOn(ItemCardapioController.class)
                        .buscarPorId(id))
                        .withSelfRel(),
                linkTo(methodOn(ItemCardapioController.class)
                        .listar(null, null))
                        .withRel("todos_itens")
        );

        return ResponseEntity.ok(model);
    }

    @Operation(
            summary = "Criar novo item no cardápio",
            description = "Cadastra um novo item no cardápio do restaurante, vinculando-o a uma categoria."
    )
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(
            @Valid @RequestBody ItemCardapioDTO dto,
            UriComponentsBuilder uriBuilder
    ) {
        ItemCardapio salvo = service.criar(dto);

        URI location = uriBuilder
                .path("/api/item-cardapio/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();

        Map<String, Object> body = Map.of(
                "mensagem", "Item do cardápio criado com sucesso!",
                "item", ItemCardapioDTO.fromEntity(salvo),
                "_links", Map.of(
                        "self", linkTo(methodOn(ItemCardapioController.class)
                                .buscarPorId(salvo.getId()))
                                .toUri(),
                        "todos_itens", linkTo(methodOn(ItemCardapioController.class)
                                .listar(null, null))
                                .toUri()
                )
        );

        return ResponseEntity.created(location).body(body);
    }

    @Operation(
            summary = "Atualizar item existente",
            description = "Atualiza as informações de um item do cardápio já cadastrado."
    )
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemCardapioDTO dto
    ) {
        ItemCardapio atualizado = service.atualizar(id, dto);

        Map<String, Object> body = Map.of(
                "mensagem", "Item do cardápio atualizado com sucesso!",
                "item", ItemCardapioDTO.fromEntity(atualizado),
                "_links", Map.of(
                        "self", linkTo(methodOn(ItemCardapioController.class)
                                .buscarPorId(atualizado.getId()))
                                .toUri(),
                        "todos_itens", linkTo(methodOn(ItemCardapioController.class)
                                .listar(null, null))
                                .toUri()
                )
        );

        return ResponseEntity.ok(body);
    }

    @Operation(
            summary = "Remover item do cardápio",
            description = "Remove um item do cardápio a partir do identificador informado."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> excluir(@PathVariable Long id) {
        service.excluir(id);

        Map<String, Object> body = Map.of(
                "mensagem", "Item do cardápio removido com sucesso!",
                "status", HttpStatus.OK.value(),
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity.ok(body);
    }
}