package com.pedix.api.service;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.PedidoItem;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.dto.PedidoItemDTO;
import com.pedix.api.dto.PedidoResponseDTO;
import com.pedix.api.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemCardapioService itemCardapioService;

    @Transactional(readOnly = true)
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll().stream()
                .sorted(Comparator.comparing(Pedido::getId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodosResponse() {
        return listarTodos().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorGarcom(String loginGarcom) {
        return pedidoRepository.findByGarcomResponsavel(loginGarcom).stream()
                .sorted(Comparator.comparing(Pedido::getId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarPorGarcomResponse(String loginGarcom) {
        return listarPorGarcom(loginGarcom).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPorComanda(Long comandaId) {
        return pedidoRepository.findByComandaId(comandaId).stream()
                .sorted(Comparator.comparing(Pedido::getId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarResponsePorId(Long id) {
        return toResponse(buscarPorId(id));
    }

    @Transactional
    public Pedido criar(PedidoDTO dto, String loginGarcom) {
        validarPedidoDTO(dto);

        Pedido pedido = new Pedido();
        pedido.setComandaId(dto.getComandaId());
        pedido.setObservacao(dto.getObservacao());
        pedido.setGarcomResponsavel(loginGarcom);
        pedido.setStatus(StatusPedido.EM_PREPARO);

        for (PedidoItemDTO itemDTO : dto.getItens()) {
            ItemCardapio itemCardapio = itemCardapioService.buscarPorId(itemDTO.getItemCardapioId());

            if (!Boolean.TRUE.equals(itemCardapio.getDisponivel())) {
                throw new IllegalArgumentException("Item indisponível: " + itemCardapio.getNome());
            }

            PedidoItem pedidoItem = PedidoItem.builder()
                    .pedido(pedido)
                    .itemCardapio(itemCardapio)
                    .quantidade(itemDTO.getQuantidade())
                    .build();

            pedidoItem.definirPrecoPadrao();
            pedidoItem.recalcularSubtotal();

            pedido.adicionarItem(pedidoItem);
        }

        pedido.recalcularTotal();
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public PedidoResponseDTO criarPedido(Long comandaId, PedidoDTO dto, String loginGarcom) {
        PedidoDTO pedidoDTO = PedidoDTO.builder()
                .comandaId(comandaId)
                .itens(dto.getItens())
                .observacao(dto.getObservacao())
                .build();

        Pedido pedido = criar(pedidoDTO, loginGarcom);
        return toResponse(pedido);
    }

    @Transactional
    public PedidoResponseDTO atualizarStatus(Long id, StatusPedido status) {
        Pedido pedido = buscarPorId(id);
        pedido.atualizarStatus(status);
        return toResponse(pedidoRepository.save(pedido));
    }

    @Transactional
    public Pedido cancelarPedido(Long id) {
        Pedido pedido = buscarPorId(id);
        pedido.cancelar();
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void excluir(Long id) {
        Pedido pedido = buscarPorId(id);
        pedidoRepository.delete(pedido);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO toResponse(Pedido pedido) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .comandaId(pedido.getComandaId())
                .status(pedido.getStatus())
                .dataCriacao(pedido.getDataHora())
                .observacao(pedido.getObservacao())
                .total(pedido.getTotal())
                .garcomResponsavel(pedido.getGarcomResponsavel())
                .itens(pedido.getItens().stream()
                        .map(item -> PedidoResponseDTO.ItemResumo.builder()
                                .itemCardapioId(item.getItemCardapio().getId())
                                .nome(item.getItemCardapio().getNome())
                                .quantidade(item.getQuantidade())
                                .precoUnitario(item.getPrecoUnitario())
                                .subtotal(item.getSubtotal())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    private void validarPedidoDTO(PedidoDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Pedido não informado.");
        }

        if (dto.getComandaId() == null || dto.getComandaId() <= 0) {
            throw new IllegalArgumentException("Comanda inválida.");
        }

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("O pedido deve conter ao menos um item.");
        }

        for (PedidoItemDTO item : dto.getItens()) {
            if (item.getItemCardapioId() == null || item.getItemCardapioId() <= 0) {
                throw new IllegalArgumentException("Item do cardápio inválido.");
            }

            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade inválida para o item.");
            }
        }
    }
}