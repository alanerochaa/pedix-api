package com.pedix.api.controller;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.dto.PedidoResponseDTO;
import com.pedix.api.service.PedidoService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/pedido")
@RequiredArgsConstructor
@Tag(
        name = "🧾 Pedido",
        description = """
        Controla os **pedidos** vinculados às comandas do restaurante.  
        Permite **criar pedidos**, **listar por comanda**, **buscar por ID** e **atualizar status**.
        """
)
public class PedidoController {

    private final PedidoService service;

    // BUSCAR PEDIDO POR ID
    @Operation(summary = "🔍 Buscar pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obter(@PathVariable Long id) {
        Pedido pedido = service.buscarPorId(id);
        return ResponseEntity.ok(service.toResponse(pedido));
    }

    // LISTAR PEDIDOS POR COMANDA
    @Operation(summary = "📋 Listar pedidos por comanda")
    @GetMapping("/comanda/{comandaId}")
    public ResponseEntity<List<Pedido>> listarPorComanda(@PathVariable Long comandaId) {
        List<Pedido> pedidos = service.listarPorComanda(comandaId);
        return ResponseEntity.ok(pedidos);
    }

    // CRIAR NOVO PEDIDO
    @Operation(summary = "➕ Criar novo pedido")
    @PostMapping("/comanda/{comandaId}")
    public ResponseEntity<Map<String, Object>> criar(
            @PathVariable Long comandaId,
            @Valid @RequestBody PedidoDTO dto,
            UriComponentsBuilder uri) {

        PedidoResponseDTO resp = service.criarPedido(comandaId, dto);
        URI location = uri.path("/api/pedido/{id}").buildAndExpand(resp.getId()).toUri();

        Map<String, Object> body = Map.of(
                "mensagem", "🧾 Pedido criado com sucesso!",
                "pedido", resp
        );
        return ResponseEntity.created(location).body(body);
    }

    // ATUALIZAR STATUS DO PEDIDO
    @Operation(summary = "🔄 Atualizar status do pedido")
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status) {

        PedidoResponseDTO atualizado = service.atualizarStatus(id, status);
        Map<String, Object> body = Map.of(
                "mensagem", "✅ Status do pedido atualizado com sucesso!",
                "pedido", atualizado
        );
        return ResponseEntity.ok(body);
    }
}
