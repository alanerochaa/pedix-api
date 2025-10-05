package com.pedix.api.controller;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.service.PedidoService;
import com.pedix.api.repository.ItemCardapioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {

    @Autowired
    private PedidoService service;

    @Autowired
    private ItemCardapioRepository itemRepository;

    @PostMapping
    public Pedido criar(@RequestBody PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setComandaId(dto.getComandaId());
        pedido.setItem(itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new RuntimeException("Item não encontrado")));
        pedido.setQuantidade(dto.getQuantidade());
        pedido.setObservacao(dto.getObservacao());
        return service.criarPedido(pedido);
    }

    @GetMapping("/comanda/{comandaId}")
    public List<Pedido> listarPorComanda(@PathVariable Long comandaId) {
        return service.listarPedidosPorComanda(comandaId);
    }

    @PutMapping("/{id}/status")
    public Pedido atualizarStatus(@PathVariable Long id, @RequestParam StatusPedido status) {
        return service.atualizarStatus(id, status);
    }
}
