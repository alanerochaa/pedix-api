package com.pedix.api.controller;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.enums.CategoriaItem;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
@RequestMapping("/api/item-cardapio")
@RequiredArgsConstructor
@Tag(
        name = "Cardápio",
        description = """
        Controla os **itens do cardápio** do restaurante.
        Permite **criar**, **listar**, **buscar**, **atualizar** e **remover** pratos, bebidas e sobremesas.
        """
)
public class ItemCardapioController {

    private final ItemCardapioService service;


    @Operation(summary = "Listar itens do cardápio")
    @GetMapping
    public ResponseEntity<List<EntityModel<ItemCardapio>>> listar(@RequestParam(required = false) CategoriaItem categoria) {
        List<ItemCardapio> itens = (categoria != null)
                ? service.listarPorCategoria(categoria)
                : service.listarDisponiveis();

        List<EntityModel<ItemCardapio>> resposta = itens.stream()
                .map(item -> EntityModel.of(item,
                        linkTo(methodOn(ItemCardapioController.class).buscarPorId(item.getId())).withSelfRel(),
                        linkTo(methodOn(ItemCardapioController.class).listar(null)).withRel("todos_itens")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @Operation(summary = "Buscar item por ID")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ItemCardapio>> buscarPorId(@PathVariable Long id) {
        ItemCardapio item = service.buscarPorId(id);

        EntityModel<ItemCardapio> model = EntityModel.of(item,
                linkTo(methodOn(ItemCardapioController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ItemCardapioController.class).listar(null)).withRel("todos_itens"));

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Criar novo item no cardápio")
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(
            @Valid @RequestBody ItemCardapioDTO dto,
            UriComponentsBuilder uriBuilder) {

        ItemCardapio salvo = service.criar(dto);
        URI location = uriBuilder.path("/api/item-cardapio/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();

        Map<String, Object> body = Map.of(
                "mensagem", "Item do cardápio criado com sucesso!",
                "item", salvo,
                "_links", Map.of(
                        "self", linkTo(methodOn(ItemCardapioController.class).buscarPorId(salvo.getId())).toUri(),
                        "todos_itens", linkTo(methodOn(ItemCardapioController.class).listar(null)).toUri()
                )
        );

        return ResponseEntity.created(location).body(body);
    }

    @Operation(summary = "Atualizar item existente")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemCardapioDTO dto) {

        ItemCardapio atualizado = service.atualizar(id, dto);

        Map<String, Object> body = Map.of(
                "mensagem", "Item do cardápio atualizado com sucesso!",
                "item", atualizado,
                "_links", Map.of(
                        "self", linkTo(methodOn(ItemCardapioController.class).buscarPorId(atualizado.getId())).toUri(),
                        "todos_itens", linkTo(methodOn(ItemCardapioController.class).listar(null)).toUri()
                )
        );

        return ResponseEntity.ok(body);
    }

    @Operation(summary = "Remover item do cardápio")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletar(@PathVariable Long id) {
        service.deletar(id);

        Map<String, Object> body = Map.of(
                "mensagem", " Item do cardápio removido com sucesso!",
                "status", HttpStatus.OK.value(),
                "timestamp", java.time.LocalDateTime.now()
        );

        return ResponseEntity.ok(body);
    }


}
