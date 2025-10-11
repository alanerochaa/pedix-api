package com.pedix.api.controller;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.dto.PedidoResponseDTO;
import com.pedix.api.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
        name = "🧾 Pedido ",
        description = """
        Controla os **pedidos** vinculados às comandas do restaurante.  
        Permite **criar pedidos**, **listar por comanda**, **buscar por ID** e **atualizar status**.
        """
)
public class PedidoController {

    private final PedidoService service;

    //  BUSCAR PEDIDO POR ID
    @Operation(
            summary = "🔍 Buscar pedido por ID",
            description = """
            Retorna os detalhes completos de um pedido específico, incluindo  
            informações dos itens, valores e status atual.
            """,
            parameters = {
                    @Parameter(name = "id", description = "ID do pedido", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido encontrado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PedidoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obter(@PathVariable Long id) {
        Pedido pedido = service.buscarPorId(id);
        return ResponseEntity.ok(service.toResponse(pedido));
    }

    // LISTAR PEDIDOS POR COMANDA
    @Operation(
            summary = "📋 Listar pedidos por comanda",
            description = """
            Retorna todos os pedidos vinculados a uma **comanda específica**.  
            Pode ser usado para visualizar o histórico ou pedidos em andamento.
            """,
            parameters = {
                    @Parameter(name = "comandaId", description = "ID da comanda", example = "1001")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Nenhum pedido encontrado para a comanda informada")
            }
    )
    @GetMapping("/comanda/{comandaId}")
    public ResponseEntity<List<Pedido>> listarPorComanda(@PathVariable Long comandaId) {
        List<Pedido> pedidos = service.listarPorComanda(comandaId);
        return ResponseEntity.ok(pedidos);
    }


    // CRIAR NOVO PEDIDO
    @Operation(
            summary = "➕ Criar novo pedido",
            description = """
            Cria um novo pedido vinculado a uma **comanda existente**,  
            incluindo seus itens, quantidades e valores calculados automaticamente.
            """,
            parameters = {
                    @Parameter(name = "comandaId", description = "ID da comanda associada ao pedido", example = "1002")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do pedido, contendo os itens e quantidades",
                    content = @Content(schema = @Schema(implementation = PedidoDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PedidoResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Erro de validação ou item indisponível")
            }
    )
    @PostMapping("/comanda/{comandaId}")
    public ResponseEntity<PedidoResponseDTO> criar(
            @PathVariable Long comandaId,
            @Valid @RequestBody PedidoDTO dto,
            UriComponentsBuilder uri) {

        PedidoResponseDTO resp = service.criarPedido(comandaId, dto);
        URI location = uri.path("/api/pedido/{id}").buildAndExpand(resp.getId()).toUri();
        return ResponseEntity.created(location).body(resp);
    }


    //  ATUALIZAR STATUS DO PEDIDO
    @Operation(
            summary = "🔄 Atualizar status do pedido",
            description = """
            Atualiza o **status atual do pedido**.  
            Os status possíveis são: 🧑‍🍳 *EM_PREPARO*, ✅ *PRONTO*, 🚚 *ENTREGUE* ou ❌ *CANCELADO*.
            """,
            parameters = {
                    @Parameter(name = "id", description = "ID do pedido", example = "1"),
                    @Parameter(name = "status", description = "Novo status do pedido", example = "PRONTO")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PedidoResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
                    @ApiResponse(responseCode = "400", description = "Status inválido ou transição não permitida")
            }
    )
    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status) {
        PedidoResponseDTO atualizado = service.atualizarStatus(id, status);
        return ResponseEntity.ok(atualizado);
    }
}
