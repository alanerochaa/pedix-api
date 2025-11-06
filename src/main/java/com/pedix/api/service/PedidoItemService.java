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

    // =========================
    // READS
    // =========================
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

    // =========================
    // WRITES
    // =========================
    @Transactional
    public PedidoItemResponseDTO criar(PedidoItemRequestDTO dto) {
        validar(dto);

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
                .precoUnitario(dto.getPrecoUnitario())
                .subtotal(calcSubtotal(dto.getPrecoUnitario(), dto.getQuantidade()))
                .build();

        // Garante consistência se preço não vier no DTO
        if (entity.getPrecoUnitario() == null) {
            entity.definirPrecoPadrao();
        } else {
            entity.recalcularSubtotal();
        }

        PedidoItem salvo = pedidoItemRepository.save(entity);

        // Recalcula total do pedido
        pedido.recalcularTotal();
        pedidoRepository.save(pedido);

        return toResponse(salvo);
    }

    @Transactional
    public PedidoItemResponseDTO atualizar(Long id, PedidoItemRequestDTO dto) {
        // Update “scoped”: somente quantidade e preço; pedidoId/itemCardapioId permanecem
        validar(dto);

        PedidoItem entity = buscarPorId(id);
        entity.setQuantidade(dto.getQuantidade());
        entity.setPrecoUnitario(dto.getPrecoUnitario());
        entity.setSubtotal(calcSubtotal(dto.getPrecoUnitario(), dto.getQuantidade()));

        // Lifecycle-safe
        entity.recalcularSubtotal();

        PedidoItem atualizado = pedidoItemRepository.save(entity);

        Pedido pedido = atualizado.getPedido();
        pedido.recalcularTotal();
        pedidoRepository.save(pedido);

        return toResponse(atualizado);
    }

    @Transactional
    public Long deletar(Long id) {
        PedidoItem entity = buscarPorId(id);
        Long pedidoId = entity.getPedido().getId();

        pedidoItemRepository.delete(entity);

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + pedidoId));
        pedido.recalcularTotal();
        pedidoRepository.save(pedido);

        return pedidoId;
    }

    // =========================
    // MAPPER
    // =========================
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

    // =========================
    // HELPERS
    // =========================
    private void validar(PedidoItemRequestDTO dto) {
        if (dto.getQuantidade() == null || dto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("Quantidade inválida: " + dto.getQuantidade());
        }
        if (dto.getPrecoUnitario() == null || dto.getPrecoUnitario().signum() < 0) {
            throw new IllegalArgumentException("Preço unitário inválido: " + dto.getPrecoUnitario());
        }
    }

    private BigDecimal calcSubtotal(BigDecimal precoUnitario, Integer quantidade) {
        return precoUnitario
                .multiply(BigDecimal.valueOf(quantidade))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
