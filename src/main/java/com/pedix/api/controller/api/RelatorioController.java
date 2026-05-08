package com.pedix.api.controller.api;

import com.pedix.api.domain.Relatorio;
import com.pedix.api.dto.RelatorioDTO;
import com.pedix.api.repository.RelatorioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioRepository repository;

    @GetMapping
    public ResponseEntity<List<RelatorioDTO>> listar() {
        var relatorios = repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(relatorios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelatorioDTO> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(relatorio -> ResponseEntity.ok(toDTO(relatorio)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<RelatorioDTO>> listarPorTipo(@PathVariable String tipo) {
        var relatorios = repository.findByTipoIgnoreCase(tipo)
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(relatorios);
    }

    @PostMapping
    public ResponseEntity<RelatorioDTO> criar(@RequestBody @Valid RelatorioDTO dto) {
        var relatorio = Relatorio.builder()
                .tipo(dto.getTipo())
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .valorTotal(dto.getValorTotal())
                .quantidade(dto.getQuantidade())
                .responsavel(dto.getResponsavel())
                .build();

        var salvo = repository.save(relatorio);

        return ResponseEntity
                .created(URI.create("/api/relatorios/" + salvo.getId()))
                .body(toDTO(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private RelatorioDTO toDTO(Relatorio relatorio) {
        return RelatorioDTO.builder()
                .id(relatorio.getId())
                .tipo(relatorio.getTipo())
                .titulo(relatorio.getTitulo())
                .descricao(relatorio.getDescricao())
                .valorTotal(relatorio.getValorTotal())
                .quantidade(relatorio.getQuantidade())
                .dataGeracao(relatorio.getDataGeracao())
                .responsavel(relatorio.getResponsavel())
                .build();
    }
}