package com.pedix.api.controller;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.enums.CategoriaItem;
import com.pedix.api.dto.ItemCardapioDTO;
import com.pedix.api.service.ItemCardapioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/item-cardapio")
@RequiredArgsConstructor
public class ItemCardapioController {

    private final ItemCardapioService service;

    @GetMapping
    public List<ItemCardapio> listar(@RequestParam(required = false) CategoriaItem categoria) {
        if (categoria != null) return service.listarPorCategoria(categoria);
        return service.listarDisponiveis();
    }

    @GetMapping("/page")
    public Page<ItemCardapio> listarPaginado(Pageable pageable) {
        return service.listarDisponiveis(pageable);
    }

    @PostMapping
    public ResponseEntity<ItemCardapio> criar(@Valid @RequestBody ItemCardapioDTO dto,
                                              UriComponentsBuilder uriBuilder) {
        ItemCardapio salvo = service.criar(dto);
        URI location = uriBuilder.path("/api/item-cardapio/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemCardapio> atualizar(@PathVariable Long id,
                                                  @Valid @RequestBody ItemCardapioDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
