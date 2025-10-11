package com.pedix.api.controller;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.enums.CategoriaItem;
import com.pedix.api.dto.ItemCardapioDTO;
import com.pedix.api.service.ItemCardapioService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/item-cardapio")
@RequiredArgsConstructor
@Tag(
        name = "🍔 Cardápio",
        description = """
        Controla os **itens do cardápio** do restaurante.  
        Permite **criar**, **listar**, **buscar**, **atualizar** e **remover** pratos, bebidas e sobremesas.
        """
)
public class ItemCardapioController {

    private final ItemCardapioService service;

    // LISTAR ITENS DO CARDÁPIO
    @Operation(summary = "📋 Listar itens do cardápio")
    @GetMapping
    public ResponseEntity<List<ItemCardapio>> listar(@RequestParam(required = false) CategoriaItem categoria) {
        List<ItemCardapio> itens = (categoria != null)
                ? service.listarPorCategoria(categoria)
                : service.listarDisponiveis();
        return ResponseEntity.ok(itens);
    }

    // BUSCAR ITEM POR ID
    @Operation(summary = "🔍 Buscar item por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemCardapio> buscarPorId(@PathVariable Long id) {
        ItemCardapio item = service.buscarPorId(id);
        return ResponseEntity.ok(item);
    }

    // CRIAR NOVO ITEM
    @Operation(summary = "➕ Criar novo item")
    @PostMapping
    public ResponseEntity<Map<String, Object>> criar(
            @Valid @RequestBody ItemCardapioDTO dto,
            UriComponentsBuilder uriBuilder) {

        ItemCardapio salvo = service.criar(dto);
        URI location = uriBuilder.path("/api/item-cardapio/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();

        Map<String, Object> body = Map.of(
                "mensagem", "🍔 Item do cardápio criado com sucesso!",
                "item", salvo
        );
        return ResponseEntity.created(location).body(body);
    }

    // ATUALIZAR ITEM
    @Operation(summary = "✏️ Atualizar item existente")
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemCardapioDTO dto) {

        ItemCardapio atualizado = service.atualizar(id, dto);
        Map<String, Object> body = Map.of(
                "mensagem", "✅ Item do cardápio atualizado com sucesso!",
                "item", atualizado
        );
        return ResponseEntity.ok(body);
    }

    // DELETAR ITEM
    @Operation(summary = "🗑️ Remover item do cardápio")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.ok(Map.of("mensagem", "🗑️ Item do cardápio removido com sucesso!"));
    }
}
