package com.pedix.api.service;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.PedidoItem;
import com.pedix.api.dto.PedidoItemRequestDTO;
import com.pedix.api.dto.PedidoItemResponseDTO;
import com.pedix.api.repository.PedidoItemRepository;
import com.pedix.api.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoItemService {

    private final PedidoItemRepository pedidoItemRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemCardapioService itemCardapioService;

    @Transactional(readOnly = true)
    public List<PedidoItemResponseDTO> listarTodosDTO() {
        return pedidoItemRepository.findAll().stream()
                .sorted(Comparator.comparing(PedidoItem::getId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoItemResponseDTO buscarDTOPorId(Long id) {
        return toResponse(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public PedidoItem buscarPorId(Long id) {
        return pedidoItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item de pedido não encontrado: " + id));
    }

    @Transactional
    public PedidoItemResponseDTO criar(PedidoItemRequestDTO dto) {
        validarCriacao(dto);

        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + dto.getPedidoId()));

        ItemCardapio itemCardapio = itemCardapioService.buscarPorId(dto.getItemCardapioId());

        if (!Boolean.TRUE.equals(itemCardapio.getDisponivel())) {
            throw new IllegalArgumentException("Item indisponível: " + itemCardapio.getNome());
        }

        PedidoItem entity = PedidoItem.builder()
                .pedido(pedido)
                .itemCardapio(itemCardapio)
                .quantidade(dto.getQuantidade())
                .build();

        if (dto.getPrecoUnitario() != null) {
            entity.setPrecoUnitario(dto.getPrecoUnitario());
        } else {
            entity.setPrecoUnitario(itemCardapio.getPreco());
        }

        entity.setSubtotal(calcSubtotal(entity.getPrecoUnitario(), entity.getQuantidade()));
        pedido.adicionarItem(entity);

        PedidoItem salvo = pedidoItemRepository.save(entity);
        pedidoRepository.save(pedido);

        return toResponse(salvo);
    }

    @Transactional
    public PedidoItemResponseDTO atualizar(Long id, PedidoItemRequestDTO dto) {
        validarAtualizacao(dto);

        PedidoItem entity = buscarPorId(id);

        if (dto.getItemCardapioId() != null
                && !dto.getItemCardapioId().equals(entity.getItemCardapio().getId())) {

            ItemCardapio novoItem = itemCardapioService.buscarPorId(dto.getItemCardapioId());

            if (!Boolean.TRUE.equals(novoItem.getDisponivel())) {
                throw new IllegalArgumentException("Item indisponível: " + novoItem.getNome());
            }

            entity.setItemCardapio(novoItem);
        }

        entity.setQuantidade(dto.getQuantidade());

        if (dto.getPrecoUnitario() != null) {
            entity.setPrecoUnitario(dto.getPrecoUnitario());
        } else {
            entity.setPrecoUnitario(entity.getItemCardapio().getPreco());
        }

        entity.setSubtotal(calcSubtotal(entity.getPrecoUnitario(), entity.getQuantidade()));

        PedidoItem atualizado = pedidoItemRepository.save(entity);

        Pedido pedido = atualizado.getPedido();
        pedido.recalcularTotal();
        pedidoRepository.save(pedido);

        return toResponse(atualizado);
    }

    @Transactional
    public Long deletar(Long id) {
        PedidoItem entity = buscarPorId(id);
        Pedido pedido = entity.getPedido();
        Long pedidoId = pedido.getId();

        pedido.removerItem(entity);
        pedidoItemRepository.delete(entity);
        pedido.recalcularTotal();
        pedidoRepository.save(pedido);

        return pedidoId;
    }

    public PedidoItemResponseDTO toResponse(PedidoItem item) {
        return PedidoItemResponseDTO.builder()
                .id(item.getId())
                .pedidoId(item.getPedido().getId())
                .itemCardapioId(item.getItemCardapio().getId())
                .nomeItem(item.getItemCardapio().getNome())
                .quantidade(item.getQuantidade())
                .precoUnitario(item.getPrecoUnitario())
                .subtotal(item.getSubtotal())
                .build();
    }

    private void validarCriacao(PedidoItemRequestDTO dto) {
        if (dto.getPedidoId() == null || dto.getPedidoId() <= 0) {
            throw new IllegalArgumentException("PedidoId inválido: " + dto.getPedidoId());
        }
        if (dto.getItemCardapioId() == null || dto.getItemCardapioId() <= 0) {
            throw new IllegalArgumentException("ItemCardapioId inválido: " + dto.getItemCardapioId());
        }
        if (dto.getQuantidade() == null || dto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade inválida: " + dto.getQuantidade());
        }
        if (dto.getPrecoUnitario() != null && dto.getPrecoUnitario().signum() < 0) {
            throw new IllegalArgumentException("Preço unitário inválido: " + dto.getPrecoUnitario());
        }
    }

    private void validarAtualizacao(PedidoItemRequestDTO dto) {
        if (dto.getQuantidade() == null || dto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade inválida: " + dto.getQuantidade());
        }
        if (dto.getPrecoUnitario() != null && dto.getPrecoUnitario().signum() < 0) {
            throw new IllegalArgumentException("Preço unitário inválido: " + dto.getPrecoUnitario());
        }
    }

    private BigDecimal calcSubtotal(BigDecimal precoUnitario, Integer quantidade) {
        return precoUnitario
                .multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);
    }
}