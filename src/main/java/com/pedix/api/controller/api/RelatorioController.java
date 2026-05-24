package com.pedix.api.controller.api;

import com.pedix.api.domain.Relatorio;
import com.pedix.api.dto.MensagemResponse;
import com.pedix.api.dto.RelatorioDTO;
import com.pedix.api.repository.RelatorioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
@Tag(
        name = "Relatórios",
        description = """
                Gerencia os relatórios operacionais e analíticos do sistema Pedix.
                Permite cadastrar, consultar, filtrar e remover relatórios relacionados às operações do restaurante.
                """
)
public class RelatorioController {

    private final RelatorioRepository repository;

    @Operation(
            summary = "Listar relatórios",
            description = "Retorna todos os relatórios cadastrados no sistema."
    )
    @GetMapping
    public ResponseEntity<List<RelatorioDTO>> listar() {

        var relatorios = repository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(relatorios);
    }

    @Operation(
            summary = "Buscar relatório por ID",
            description = "Consulta um relatório específico utilizando seu identificador único."
    )
    @GetMapping("/{id}")
    public ResponseEntity<RelatorioDTO> buscarPorId(@PathVariable Long id) {

        return repository.findById(id)
                .map(relatorio -> ResponseEntity.ok(toDTO(relatorio)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Listar relatórios por tipo",
            description = "Retorna todos os relatórios filtrados pelo tipo informado, como vendas, pedidos ou desempenho operacional."
    )
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<RelatorioDTO>> listarPorTipo(@PathVariable String tipo) {

        var relatorios = repository.findByTipoIgnoreCase(tipo)
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(relatorios);
    }

    @Operation(
            summary = "Criar relatório",
            description = "Cadastra um novo relatório operacional ou analítico no sistema."
    )
    @PostMapping
    public ResponseEntity<RelatorioDTO> criar(
            @RequestBody @Valid RelatorioDTO dto
    ) {

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

    @Operation(
            summary = "Remover relatório",
            description = "Remove um relatório cadastrado no sistema utilizando o identificador informado."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemResponse> deletar(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);

        return ResponseEntity.ok(
                new MensagemResponse("Relatório removido com sucesso.")
        );
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