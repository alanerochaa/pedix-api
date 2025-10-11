package com.pedix.api.service;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.PedidoItem;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.dto.PedidoResponseDTO;
import com.pedix.api.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemCardapioService itemService;

    //  Listar todos os pedidos
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    //  Buscar pedido por ID
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));
    }

    //  Listar pedidos por comanda
    public List<Pedido> listarPorComanda(Long comandaId) {
        return pedidoRepository.findByComandaId(comandaId);
    }

    //  Criar novo pedido
    @Transactional
    public PedidoResponseDTO criarPedido(Long comandaId, PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setComandaId(comandaId);
        pedido.setStatus(StatusPedido.EM_PREPARO);
        pedido.setObservacao(dto.getObservacao());

        // Adicionar os itens do pedido
        dto.getItens().forEach(itemDTO -> {
            ItemCardapio item = itemService.buscarPorId(itemDTO.getItemCardapioId());

            if (!Boolean.TRUE.equals(item.getDisponivel())) {
                throw new IllegalArgumentException("Item indisponível: " + item.getNome());
            }

            PedidoItem pedidoItem = new PedidoItem();
            pedidoItem.setItemCardapio(item);
            pedidoItem.setQuantidade(itemDTO.getQuantidade());
            pedidoItem.definirPrecoPadrao();
            pedido.addItem(pedidoItem);
        });

        Pedido salvo = pedidoRepository.save(pedido);
        return toResponse(salvo);
    }

    //  Atualizar status do pedido
    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(status);
        Pedido atualizado = pedidoRepository.save(pedido);
        return toResponse(atualizado);
    }

    //  Deletar pedido por ID
    @Transactional
    public void deletarPedido(Long id) {
        Pedido pedido = buscarPorId(id);
        pedidoRepository.delete(pedido);
    }

    //  Converter Pedido -> PedidoResponseDTO
    public PedidoResponseDTO toResponse(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .comandaId(pedido.getComandaId())
                .status(pedido.getStatus())
                .dataCriacao(pedido.getDataHora())
                .observacao(pedido.getObservacao())
                .total(pedido.getTotal())
                .itens(
                        pedido.getItens().stream()
                                .map(pi -> PedidoResponseDTO.ItemResumo.builder()
                                        .itemCardapioId(pi.getItemCardapio().getId())
                                        .nome(pi.getItemCardapio().getNome())
                                        .quantidade(pi.getQuantidade())
                                        .precoUnitario(pi.getPrecoUnitario())
                                        .subtotal(pi.getSubtotal())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }
}
