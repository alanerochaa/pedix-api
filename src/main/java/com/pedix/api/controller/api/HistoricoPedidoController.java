package com.pedix.api.controller.api;

import com.pedix.api.domain.HistoricoPedido;
import com.pedix.api.dto.HistoricoPedidoDTO;
import com.pedix.api.repository.HistoricoPedidoRepository;
import com.pedix.api.repository.PedidoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/historicos-pedidos")
@RequiredArgsConstructor
public class HistoricoPedidoController {

    private final HistoricoPedidoRepository historicoRepository;
    private final PedidoRepository pedidoRepository;

    @GetMapping
    public ResponseEntity<List<HistoricoPedidoDTO>> listar() {
        var historicos = historicoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(historicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistoricoPedidoDTO> buscarPorId(@PathVariable Long id) {
        return historicoRepository.findById(id)
                .map(historico -> ResponseEntity.ok(toDTO(historico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<HistoricoPedidoDTO>> listarPorPedido(@PathVariable Long pedidoId) {
        var historicos = historicoRepository.findByPedidoId(pedidoId)
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(historicos);
    }

    @PostMapping
    public ResponseEntity<HistoricoPedidoDTO> criar(@RequestBody @Valid HistoricoPedidoDTO dto) {
        var pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));

        var historico = HistoricoPedido.builder()
                .pedido(pedido)
                .statusAnterior(dto.getStatusAnterior())
                .statusNovo(dto.getStatusNovo())
                .descricao(dto.getDescricao())
                .usuario(dto.getUsuario())
                .build();

        var salvo = historicoRepository.save(historico);

        return ResponseEntity
                .created(URI.create("/api/historicos-pedidos/" + salvo.getId()))
                .body(toDTO(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!historicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        historicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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