package com.pedix.api.service;

import com.pedix.api.domain.Relatorio;
import com.pedix.api.dto.RelatorioDTO;
import com.pedix.api.repository.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final RelatorioRepository repository;

    public List<RelatorioDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public RelatorioDTO buscarPorId(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Relatório não encontrado."));
    }

    public List<RelatorioDTO> listarPorTipo(String tipo) {
        return repository.findByTipoIgnoreCase(tipo).stream().map(this::toDTO).toList();
    }

    public RelatorioDTO criar(RelatorioDTO dto) {
        var relatorio = Relatorio.builder()
                .tipo(dto.getTipo())
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .valorTotal(dto.getValorTotal())
                .quantidade(dto.getQuantidade())
                .responsavel(dto.getResponsavel())
                .build();

        return toDTO(repository.save(relatorio));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Relatório não encontrado.");
        }

        repository.deleteById(id);
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