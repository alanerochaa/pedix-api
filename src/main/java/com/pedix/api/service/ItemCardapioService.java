package com.pedix.api.service;

import com.pedix.api.domain.CategoriaCardapio;
import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.dto.ItemCardapioDTO;
import com.pedix.api.repository.CategoriaCardapioRepository;
import com.pedix.api.repository.ItemCardapioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemCardapioService {

    private final ItemCardapioRepository itemCardapioRepository;
    private final CategoriaCardapioRepository categoriaCardapioRepository;

    @Transactional(readOnly = true)
    public List<ItemCardapio> listarDisponiveis() {
        return itemCardapioRepository.findByDisponivelTrue().stream()
                .sorted(Comparator.comparing(ItemCardapio::getNome))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemCardapio> buscarDisponiveisPorNome(String busca) {
        if (busca == null || busca.trim().isEmpty()) {
            return listarDisponiveis();
        }

        return itemCardapioRepository.findByDisponivelTrueAndNomeContainingIgnoreCase(busca.trim()).stream()
                .sorted(Comparator.comparing(ItemCardapio::getNome))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemCardapio> listarPorCategoria(Long categoriaId) {
        return itemCardapioRepository.findByDisponivelTrueAndCategoriaId(categoriaId).stream()
                .sorted(Comparator.comparing(ItemCardapio::getNome))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemCardapio buscarPorId(Long id) {
        return itemCardapioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado: " + id));
    }

    @Transactional
    public ItemCardapio criar(ItemCardapioDTO dto) {
        CategoriaCardapio categoria = buscarCategoriaPorId(dto.getCategoriaId());

        ItemCardapio item = ItemCardapio.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .categoria(categoria)
                .preco(dto.getPreco())
                .disponivel(dto.getDisponivel() != null ? dto.getDisponivel() : true)
                .imagemUrl(dto.getImagemUrl())
                .build();

        return itemCardapioRepository.save(item);
    }

    @Transactional
    public ItemCardapio atualizar(Long id, ItemCardapioDTO dto) {
        ItemCardapio item = buscarPorId(id);
        CategoriaCardapio categoria = buscarCategoriaPorId(dto.getCategoriaId());

        item.atualizarInformacoes(
                dto.getNome(),
                dto.getDescricao(),
                categoria,
                dto.getPreco(),
                dto.getDisponivel(),
                dto.getImagemUrl()
        );

        return itemCardapioRepository.save(item);
    }

    @Transactional
    public void excluir(Long id) {
        ItemCardapio item = buscarPorId(id);
        itemCardapioRepository.delete(item);
    }

    private CategoriaCardapio buscarCategoriaPorId(Long categoriaId) {
        return categoriaCardapioRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria do cardápio não encontrada: " + categoriaId));
    }
}