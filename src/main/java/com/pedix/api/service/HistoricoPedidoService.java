package com.pedix.api.service;

import com.pedix.api.domain.HistoricoPedido;
import com.pedix.api.dto.HistoricoPedidoDTO;
import com.pedix.api.repository.HistoricoPedidoRepository;
import com.pedix.api.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoPedidoService {

    private final HistoricoPedidoRepository historicoRepository;
    private final PedidoRepository pedidoRepository;

    public List<HistoricoPedidoDTO> listar() {
        return historicoRepository.findAll().stream().map(this::toDTO).toList();
    }

    public HistoricoPedidoDTO buscarPorId(Long id) {
        return historicoRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Histórico do pedido não encontrado."));
    }

    public List<HistoricoPedidoDTO> listarPorPedido(Long pedidoId) {
        return historicoRepository.findByPedidoId(pedidoId).stream().map(this::toDTO).toList();
    }

    public HistoricoPedidoDTO criar(HistoricoPedidoDTO dto) {
        var pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));

        var historico = HistoricoPedido.builder()
                .pedido(pedido)
                .statusAnterior(dto.getStatusAnterior())
                .statusNovo(dto.getStatusNovo())
                .descricao(dto.getDescricao())
                .usuario(dto.getUsuario())
                .build();

        return toDTO(historicoRepository.save(historico));
    }

    public void deletar(Long id) {
        if (!historicoRepository.existsById(id)) {
            throw new RuntimeException("Histórico do pedido não encontrado.");
        }

        historicoRepository.deleteById(id);
    }

    private HistoricoPedidoDTO toDTO(HistoricoPedido historico) {
        return HistoricoPedidoDTO.builder()
                .id(historico.getId())
                .pedidoId(historico.getPedido() != null ? historico.getPedido().getId() : null)
                .statusAnterior(historico.getStatusAnterior())
                .statusNovo(historico.getStatusNovo())
                .descricao(historico.getDescricao())
                .dataRegistro(historico.getDataRegistro())
                .usuario(historico.getUsuario())
                .build();
    }
}