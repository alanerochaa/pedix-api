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

    public java.util.List<Pedido> listarPorComanda(Long comandaId) {
        return pedidoRepository.findByComandaId(comandaId);
    }

    @Transactional
    public PedidoResponseDTO criarPedido(Long comandaId, PedidoDTO dto) {
        // TODO (opcional): validar comanda via API C#
        Pedido pedido = Pedido.builder()
                .comandaId(comandaId)
                .observacao(dto.getObservacao())
                .status(StatusPedido.EM_PREPARO)
                .build();

        dto.getItens().forEach(i -> {
            ItemCardapio item = itemService.buscarPorId(i.getItemId());
            PedidoItem pi = PedidoItem.builder()
                    .item(item)
                    .quantidade(i.getQuantidade())
                    .precoUnitario(item.getPreco())
                    .build();
            pedido.addItem(pi);
        });

        pedido.recalcTotal();
        Pedido salvo = pedidoRepository.save(pedido);
        return toResponse(salvo);
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {
        Pedido p = buscarPorId(id);
        p.setStatus(status);
        return toResponse(pedidoRepository.save(p));
    }

    public PedidoResponseDTO toResponse(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .comandaId(pedido.getComandaId())
                .status(pedido.getStatus())
                .dataHora(pedido.getDataHora())
                .observacao(pedido.getObservacao())
                .total(pedido.getTotal())
                .itens(pedido.getItens().stream().map(pi ->
                        new PedidoResponseDTO.ItemResumo(
                                pi.getItem().getId(),
                                pi.getItem().getNome(),
                                pi.getQuantidade(),
                                pi.getPrecoUnitario(),
                                pi.getSubtotal()
                        )
                ).collect(Collectors.toList()))
                .build();
    }
}
