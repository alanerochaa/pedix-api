package com.pedix.api.controller.api;

import com.pedix.api.domain.Avaliacao;
import com.pedix.api.dto.AvaliacaoDTO;
import com.pedix.api.dto.MensagemResponse;
import com.pedix.api.repository.AvaliacaoRepository;
import com.pedix.api.repository.ItemCardapioRepository;
import com.pedix.api.repository.PedidoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
@Tag(
        name = "Avaliações",
        description = "Permite registrar, listar, consultar e remover avaliações dos clientes sobre pedidos e itens do cardápio."
)
public class AvaliacaoController {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemCardapioRepository itemCardapioRepository;

    @Operation(
            summary = "Listar avaliações",
            description = "Retorna todas as avaliações cadastradas pelos clientes, incluindo nota, comentário, pedido e item do cardápio relacionado."
    )
    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> listar() {
        var avaliacoes = avaliacaoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(avaliacoes);
    }

    @Operation(
            summary = "Buscar avaliação por ID",
            description = "Consulta uma avaliação específica a partir do seu identificador único."
    )
    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> buscarPorId(@PathVariable Long id) {
        return avaliacaoRepository.findById(id)
                .map(avaliacao -> ResponseEntity.ok(toDTO(avaliacao)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Criar avaliação",
            description = "Registra uma nova avaliação de cliente, podendo vincular a avaliação a um pedido e/ou a um item do cardápio."
    )
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> criar(@RequestBody @Valid AvaliacaoDTO dto) {

        var avaliacao = Avaliacao.builder()
                .nomeCliente(dto.getNomeCliente())
                .nota(dto.getNota())
                .comentario(dto.getComentario())
                .build();

        if (dto.getPedidoId() != null) {
            var pedido = pedidoRepository.findById(dto.getPedidoId())
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado."));

            avaliacao.setPedido(pedido);
        }

        if (dto.getItemCardapioId() != null) {
            var item = itemCardapioRepository.findById(dto.getItemCardapioId())
                    .orElseThrow(() -> new RuntimeException("Item do cardápio não encontrado."));

            avaliacao.setItemCardapio(item);
        }

        var salva = avaliacaoRepository.save(avaliacao);

        return ResponseEntity
                .created(URI.create("/api/avaliacoes/" + salva.getId()))
                .body(toDTO(salva));
    }

    @Operation(
            summary = "Remover avaliação",
            description = "Remove uma avaliação cadastrada a partir do seu identificador único."
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<MensagemResponse> deletar(@PathVariable Long id) {

        if (!avaliacaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        avaliacaoRepository.deleteById(id);

        return ResponseEntity.ok(
                new MensagemResponse("Avaliação removida com sucesso.")
        );
    }

    private AvaliacaoDTO toDTO(Avaliacao avaliacao) {

        return AvaliacaoDTO.builder()
                .id(avaliacao.getId())
                .pedidoId(avaliacao.getPedido() != null ? avaliacao.getPedido().getId() : null)
                .itemCardapioId(avaliacao.getItemCardapio() != null ? avaliacao.getItemCardapio().getId() : null)
                .nomeCliente(avaliacao.getNomeCliente())
                .nota(avaliacao.getNota())
                .comentario(avaliacao.getComentario())
                .dataAvaliacao(avaliacao.getDataAvaliacao())
                .build();
    }
}