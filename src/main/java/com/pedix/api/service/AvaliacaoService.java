package com.pedix.api.service;

import com.pedix.api.domain.Avaliacao;
import com.pedix.api.dto.AvaliacaoDTO;
import com.pedix.api.repository.AvaliacaoRepository;
import com.pedix.api.repository.ItemCardapioRepository;
import com.pedix.api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemCardapioRepository itemCardapioRepository;

    public List<AvaliacaoDTO> listar() {
        return avaliacaoRepository.findAll().stream().map(this::toDTO).toList();
    }

    public AvaliacaoDTO buscarPorId(Long id) {
        return avaliacaoRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));
    }

    public List<AvaliacaoDTO> listarPorPedido(Long pedidoId) {
        return avaliacaoRepository.findByPedidoId(pedidoId).stream().map(this::toDTO).toList();
    }

    public List<AvaliacaoDTO> listarPorItemCardapio(Long itemCardapioId) {
        return avaliacaoRepository.findByItemCardapioId(itemCardapioId).stream().map(this::toDTO).toList();
    }

    public AvaliacaoDTO criar(AvaliacaoDTO dto) {
        var avaliacao = Avaliacao.builder()
                .nomeCliente(dto.getNomeCliente())
                .nota(dto.getNota())
                .comentario(dto.getComentario())
                .build();

        if (dto.getPedidoId() != null) {
            var pedido = pedidoRepository.findById(dto.getPedidoId())
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));
            avaliacao.setPedido(pedido);
        }

        if (dto.getItemCardapioId() != null) {
            var item = itemCardapioRepository.findById(dto.getItemCardapioId())
                    .orElseThrow(() -> new RuntimeException("Item do cardápio não encontrado."));
            avaliacao.setItemCardapio(item);
        }

        return toDTO(avaliacaoRepository.save(avaliacao));
    }

    public void deletar(Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            throw new RuntimeException("Avaliação não encontrada.");
        }

        avaliacaoRepository.deleteById(id);
    }

    private AvaliacaoDTO toDTO(Avaliacao avaliacao) {
        return AvaliacaoDTO.builder()
                .id(avaliacao.getId())
                .pedidoId(avaliacao.getPedido() != null ? avaliacao.getPedido().getId() : null)
                .itemCardapioId(avaliacao.getItemCardapio() != null ? avaliacao.getItemCardapio().getId() : null)
                .nomeCliente(avaliacao.getNomeCliente())
                .nota(avaliacao.getNota())
                .comentario(avaliacao.getComentario())
                .dataAvaliacao(avaliacao.getDataAvaliacao())
                .build();
    }
}