package com.pedix.api.service;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.enums.CategoriaItem;
import com.pedix.api.dto.ItemCardapioDTO;
import com.pedix.api.repository.ItemCardapioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCardapioService {
    private final ItemCardapioRepository repository;

    public List<ItemCardapio> listarDisponiveis() {
        return repository.findByDisponivelTrue();
    }

    public Page<ItemCardapio> listarDisponiveis(Pageable pageable) {
        return repository.findByDisponivelTrue(pageable);
    }

    public List<ItemCardapio> listarPorCategoria(CategoriaItem categoria) {
        return repository.findByCategoriaAndDisponivelTrue(categoria);
    }

    @Transactional
    public ItemCardapio criar(ItemCardapioDTO dto) {
        ItemCardapio e = ItemCardapio.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .preco(dto.getPreco())
                .categoria(dto.getCategoria())
                .disponivel(dto.getDisponivel() == null ? true : dto.getDisponivel())
                .imagemUrl(dto.getImagemUrl())
                .build();
        return repository.save(e);
    }

    @Transactional
    public ItemCardapio atualizar(Long id, ItemCardapioDTO dto) {
        ItemCardapio e = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado: " + id));
        e.setNome(dto.getNome());
        e.setDescricao(dto.getDescricao());
        e.setPreco(dto.getPreco());
        e.setCategoria(dto.getCategoria());
        if (dto.getDisponivel() != null) e.setDisponivel(dto.getDisponivel());
        e.setImagemUrl(dto.getImagemUrl());
        return repository.save(e);
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id))
            throw new EntityNotFoundException("Item do cardápio não encontrado: " + id);
        repository.deleteById(id);
    }

    public ItemCardapio buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado: " + id));
    }
}
