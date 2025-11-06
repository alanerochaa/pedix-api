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


    public ItemCardapio buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado: " + id));
    }


    @Transactional
    public ItemCardapio criar(ItemCardapioDTO dto) {
        ItemCardapio item = converterDtoParaEntidade(dto);
        return repository.save(item);
    }


    @Transactional
    public ItemCardapio atualizar(Long id, ItemCardapioDTO dto) {
        ItemCardapio itemExistente = buscarPorId(id);

        itemExistente.setNome(dto.getNome());
        itemExistente.setDescricao(dto.getDescricao());
        itemExistente.setCategoria(dto.getCategoria());
        itemExistente.setPreco(dto.getPreco());
        itemExistente.setDisponivel(dto.getDisponivel());

        return repository.save(itemExistente);
    }

    @Transactional
    public void deletar(Long id) {
        ItemCardapio item = buscarPorId(id);
        repository.delete(item);
    }

    private ItemCardapio converterDtoParaEntidade(ItemCardapioDTO dto) {
        ItemCardapio item = new ItemCardapio();
        item.setNome(dto.getNome());
        item.setDescricao(dto.getDescricao());
        item.setCategoria(dto.getCategoria());
        item.setPreco(dto.getPreco());
        item.setDisponivel(dto.getDisponivel() != null ? dto.getDisponivel() : Boolean.TRUE);
        return item;
    }
}
