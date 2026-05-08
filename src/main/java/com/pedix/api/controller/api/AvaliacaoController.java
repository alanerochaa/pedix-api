package com.pedix.api.controller.api;

import com.pedix.api.domain.Avaliacao;
import com.pedix.api.dto.AvaliacaoDTO;
import com.pedix.api.repository.AvaliacaoRepository;
import com.pedix.api.repository.ItemCardapioRepository;
import com.pedix.api.repository.PedidoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoRepository avaliacaoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemCardapioRepository itemCardapioRepository;

    @GetMapping
    public ResponseEntity<List<AvaliacaoDTO>> listar() {
        var avaliacoes = avaliacaoRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();

        return ResponseEntity.ok(avaliacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvaliacaoDTO> buscarPorId(@PathVariable Long id) {
        return avaliacaoRepository.findById(id)
                .map(avaliacao -> ResponseEntity.ok(toDTO(avaliacao)))
                .orElse(ResponseEntity.notFound().build());
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!avaliacaoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        avaliacaoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
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