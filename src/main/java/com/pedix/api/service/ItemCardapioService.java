package com.pedix.api.service;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.enums.CategoriaItem;
import com.pedix.api.dto.ItemCardapioDTO;
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
    public List<ItemCardapio> listarPorCategoria(CategoriaItem categoria) {
        return itemCardapioRepository.findByDisponivelTrueAndCategoria(categoria).stream()
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
        ItemCardapio item = ItemCardapio.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .categoria(dto.getCategoria())
                .preco(dto.getPreco())
                .disponivel(dto.getDisponivel() != null ? dto.getDisponivel() : true)
                .imagemUrl(dto.getImagemUrl())
                .build();

        return itemCardapioRepository.save(item);
    }

    @Transactional
    public ItemCardapio atualizar(Long id, ItemCardapioDTO dto) {
        ItemCardapio item = buscarPorId(id);

        item.atualizarInformacoes(
                dto.getNome(),
                dto.getDescricao(),
                dto.getCategoria(),
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
}