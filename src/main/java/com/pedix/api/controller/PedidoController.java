package com.pedix.api.controller;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.dto.PedidoResponseDTO;
import com.pedix.api.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService service;

    @GetMapping("/{id}")
    public PedidoResponseDTO obter(@PathVariable Long id) {
        Pedido p = service.buscarPorId(id);
        return service.toResponse(p);
    }

    @GetMapping("/comanda/{comandaId}")
    public List<Pedido> listarPorComanda(@PathVariable Long comandaId) {
        return service.listarPorComanda(comandaId);
    }

    @PostMapping("/comanda/{comandaId}")
    public ResponseEntity<PedidoResponseDTO> criar(@PathVariable Long comandaId,
                                                   @Valid @RequestBody PedidoDTO dto,
                                                   UriComponentsBuilder uri) {
        PedidoResponseDTO resp = service.criarPedido(comandaId, dto);
        URI location = uri.path("/api/pedido/{id}").buildAndExpand(resp.getId()).toUri();
        return ResponseEntity.created(location).body(resp);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                             @RequestParam StatusPedido status) {
        return ResponseEntity.ok(service.atualizarStatus(id, status));
    }
}
