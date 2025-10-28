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

/**
 * Camada de serviço responsável pela regra de negócio dos pedidos.
 */
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemCardapioService itemService;

    /**
     * Lista todos os pedidos cadastrados.
     */
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    /**
     * Busca um pedido por ID, lançando exceção se não encontrado.
     */
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));
    }

    /**
     * Lista pedidos filtrando por comanda vinculada.
     */
    public List<Pedido> listarPorComanda(Long comandaId) {
        return pedidoRepository.findByComandaId(comandaId);
    }

    /**
     * Cria um novo pedido vinculado a uma comanda, validando disponibilidade dos itens.
     */
    @Transactional
    public PedidoResponseDTO criarPedido(Long comandaId, PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setComandaId(comandaId);
        pedido.setStatus(StatusPedido.EM_PREPARO);
        pedido.setObservacao(dto.getObservacao());

        // Adiciona cada item do DTO ao pedido
        dto.getItens().forEach(itemDTO -> {
            ItemCardapio item = itemService.buscarPorId(itemDTO.getItemCardapioId());

            if (!Boolean.TRUE.equals(item.getDisponivel())) {
                throw new IllegalArgumentException("Item indisponível: " + item.getNome());
            }

            PedidoItem pedidoItem = PedidoItem.builder()
                    .itemCardapio(item)
                    .quantidade(itemDTO.getQuantidade())
                    .build();

            pedidoItem.definirPrecoPadrao(); // define preço e subtotal
            pedido.adicionarItem(pedidoItem); // adiciona item e recalcula total
        });

        // Recalcula total final antes de persistir
        pedido.recalcularTotal();

        Pedido salvo = pedidoRepository.save(pedido);
        return toResponse(salvo);
    }

    /**
     * Atualiza o status de um pedido.
     */
    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {
        Pedido pedido = buscarPorId(id);
        pedido.atualizarStatus(status);
        Pedido atualizado = pedidoRepository.save(pedido);
        return toResponse(atualizado);
    }

    /**
     * Deleta um pedido existente.
     */
    @Transactional
    public void deletarPedido(Long id) {
        Pedido pedido = buscarPorId(id);
        pedidoRepository.delete(pedido);
    }

    /**
     * Converte entidade Pedido para DTO de resposta.
     */
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
