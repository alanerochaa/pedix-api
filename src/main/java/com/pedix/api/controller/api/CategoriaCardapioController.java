package com.pedix.api.controller.api;

import com.pedix.api.domain.CategoriaCardapio;
import com.pedix.api.dto.CategoriaCardapioDTO;
import com.pedix.api.repository.CategoriaCardapioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias-cardapio")
@RequiredArgsConstructor
public class CategoriaCardapioController {

    private final CategoriaCardapioRepository repository;

    @GetMapping
    public ResponseEntity<List<CategoriaCardapioDTO>> listar() {
        var categorias = repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaCardapioDTO> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(categoria -> ResponseEntity.ok(toDTO(categoria)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaCardapioDTO> criar(@RequestBody @Valid CategoriaCardapioDTO dto) {
        var categoria = CategoriaCardapio.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .build();

        var salva = repository.save(categoria);

        return ResponseEntity
                .created(URI.create("/api/categorias-cardapio/" + salva.getId()))
                .body(toDTO(salva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaCardapioDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid CategoriaCardapioDTO dto
    ) {
        return repository.findById(id)
                .map(categoria -> {
                    categoria.atualizarInformacoes(
                            dto.getNome(),
                            dto.getDescricao(),
                            dto.getAtivo()
                    );

                    var atualizada = repository.save(categoria);
                    return ResponseEntity.ok(toDTO(atualizada));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CategoriaCardapioDTO toDTO(CategoriaCardapio categoria) {
        return CategoriaCardapioDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .ativo(categoria.getAtivo())
                .build();
    }
}