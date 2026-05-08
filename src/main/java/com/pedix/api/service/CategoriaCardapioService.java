package com.pedix.api.service;

import com.pedix.api.domain.CategoriaCardapio;
import com.pedix.api.dto.CategoriaCardapioDTO;
import com.pedix.api.repository.CategoriaCardapioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaCardapioService {

    private final CategoriaCardapioRepository repository;

    public List<CategoriaCardapioDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public CategoriaCardapioDTO buscarPorId(Long id) {
        return repository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));
    }

    public CategoriaCardapioDTO criar(CategoriaCardapioDTO dto) {
        var categoria = CategoriaCardapio.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .ativo(dto.getAtivo() != null ? dto.getAtivo() : true)
                .build();

        return toDTO(repository.save(categoria));
    }

    public CategoriaCardapioDTO atualizar(Long id, CategoriaCardapioDTO dto) {
        var categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        categoria.atualizarInformacoes(dto.getNome(), dto.getDescricao(), dto.getAtivo());

        return toDTO(repository.save(categoria));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada.");
        }

        repository.deleteById(id);
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