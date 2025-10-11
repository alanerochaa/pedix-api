package com.pedix.api.controller;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.dto.PedidoResponseDTO;
import com.pedix.api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedido")
@RequiredArgsConstructor
@Tag(
        name = "🧾 Pedido",
        description = """
        Controla os **pedidos** vinculados às comandas do restaurante.  
        Permite **criar pedidos**, **listar por comanda**, **listar todos**, **buscar por ID** e **atualizar status**.
        """
)
public class PedidoController {

    private final PedidoService service;

    // 📦 LISTAR TODOS OS PEDIDOS
    @Operation(summary = "📦 Listar todos os pedidos")
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> listarTodos() {
        List<PedidoResponseDTO> resposta = service.listarTodos()
                .stream()
                .map(service::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }


    // 🔍 BUSCAR PEDIDO POR ID
    @Operation(summary = "🔍 Buscar pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obter(@PathVariable Long id) {
        Pedido pedido = service.buscarPorId(id);
        return ResponseEntity.ok(service.toResponse(pedido));
    }

    // 📋 LISTAR PEDIDOS POR COMANDA
    @Operation(summary = "📋 Listar pedidos por comanda")
    @GetMapping("/comanda/{comandaId}")
    public ResponseEntity<List<PedidoResponseDTO>> listarPorComanda(@PathVariable Long comandaId) {
        List<Pedido> pedidos = service.listarPorComanda(comandaId);
        List<PedidoResponseDTO> resposta = pedidos.stream()
                .map(service::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resposta);
    }

    // ➕ CRIAR NOVO PEDIDO
    @Operation(summary = "➕ Criar novo pedido vinculado a uma comanda")
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

    // 🔄 ATUALIZAR STATUS DO PEDIDO
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

    // 🗑️ DELETAR PEDIDO POR ID
    @Operation(summary = "🗑️ Deletar pedido por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        service.deletarPedido(id);
        Map<String, String> resposta = Map.of("mensagem", "🗑️ Pedido removido com sucesso!");
        return ResponseEntity.ok(resposta);
    }

}
