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


    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));
    }

    public List<Pedido> listarPorComanda(Long comandaId) {
        return pedidoRepository.findByComandaId(comandaId);
    }


    @Transactional
    public PedidoResponseDTO criarPedido(Long comandaId, PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setComandaId(comandaId);
        pedido.setStatus(StatusPedido.EM_PREPARO);

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

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {
        Pedido pedido = buscarPorId(id);
        pedido.setStatus(status);
        Pedido atualizado = pedidoRepository.save(pedido);
        return toResponse(atualizado);
    }

    public PedidoResponseDTO toResponse(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .comandaId(pedido.getComandaId())
                .status(pedido.getStatus())
                .dataCriacao(pedido.getDataHora())
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
