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

    /**
     * Lista todos os itens disponíveis
     */
    public List<ItemCardapio> listarDisponiveis() {
        return repository.findByDisponivelTrue();
    }

    /**
     * Lista itens de forma paginada
     */
    public Page<ItemCardapio> listarDisponiveis(Pageable pageable) {
        return repository.findByDisponivelTrue(pageable);
    }

    /**
     * Lista itens por categoria
     */
    public List<ItemCardapio> listarPorCategoria(CategoriaItem categoria) {
        return repository.findByCategoriaAndDisponivelTrue(categoria);
    }

    /**
     * Busca item por ID (ou lança exceção)
     */
    public ItemCardapio buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado: " + id));
    }

    /**
     * Cria um novo item de cardápio
     */
    @Transactional
    public ItemCardapio criar(ItemCardapioDTO dto) {
        ItemCardapio item = new ItemCardapio();
        item.setNome(dto.getNome());
        item.setDescricao(dto.getDescricao());
        item.setCategoria(dto.getCategoria());
        item.setPreco(dto.getPreco());
        item.setDisponivel(dto.getDisponivel());
        return repository.save(item);
    }

    /**
     * Atualiza um item existente
     */
    @Transactional
    public ItemCardapio atualizar(Long id, ItemCardapioDTO dto) {
        ItemCardapio item = buscarPorId(id);
        item.setNome(dto.getNome());
        item.setDescricao(dto.getDescricao());
        item.setCategoria(dto.getCategoria());
        item.setPreco(dto.getPreco());
        item.setDisponivel(dto.getDisponivel());
        return repository.save(item);
    }

    /**
     * Deleta (ou desativa) um item do cardápio
     */
    @Transactional
    public void deletar(Long id) {
        ItemCardapio item = buscarPorId(id);
        // remoção física (delete do banco)
        repository.delete(item);
        // ou, se preferir desativar:
        // item.desativar();
        // repository.save(item);
    }
}
