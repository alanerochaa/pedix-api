package com.pedix.api.controller.api;

import com.pedix.api.domain.CategoriaCardapio;
import com.pedix.api.dto.CategoriaCardapioDTO;
import com.pedix.api.dto.MensagemResponse;
import com.pedix.api.repository.CategoriaCardapioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias-cardapio")
@RequiredArgsConstructor
@Tag(
        name = "Categorias do Cardápio",
        description = "Gerencia as categorias utilizadas para organizar os itens do cardápio, como pratos, bebidas e sobremesas."
)
public class CategoriaCardapioController {

    private final CategoriaCardapioRepository repository;

    @Operation(
            summary = "Listar categorias",
            description = "Retorna todas as categorias cadastradas no sistema de cardápio."
    )
    @GetMapping
    public ResponseEntity<List<CategoriaCardapioDTO>> listar() {

        var categorias = repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(categorias);
    }

    @Operation(
            summary = "Buscar categoria por ID",
            description = "Consulta uma categoria específica utilizando seu identificador único."
    )
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaCardapioDTO> buscarPorId(@PathVariable Long id) {

        return repository.findById(id)
                .map(categoria -> ResponseEntity.ok(toDTO(categoria)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Criar categoria",
            description = "Cadastra uma nova categoria para organização dos itens do cardápio."
    )
    @PostMapping
    public ResponseEntity<CategoriaCardapioDTO> criar(
            @RequestBody @Valid CategoriaCardapioDTO dto
    ) {

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

    @Operation(
            summary = "Atualizar categoria",
            description = "Atualiza as informações de uma categoria existente no sistema."
    )
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

    @Operation(
            summary = "Remover categoria",
            description = "Remove uma categoria cadastrada no sistema através do identificador informado."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemResponse> deletar(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);

        return ResponseEntity.ok(
                new MensagemResponse("Categoria removida com sucesso.")
        );
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