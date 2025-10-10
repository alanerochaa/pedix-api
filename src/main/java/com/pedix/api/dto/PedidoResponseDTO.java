package com.pedix.api.dto;

import com.pedix.api.domain.enums.StatusPedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoResponseDTO {
    private Long id;
    private Long comandaId;
    private StatusPedido status;
    private LocalDateTime dataHora;
    private String observacao;
    private BigDecimal total;
    private List<ItemResumo> itens;

    @Getter @Setter @AllArgsConstructor
    public static class ItemResumo {
        private Long itemId;
        private String nome;
        private Integer quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal subtotal;
    }
}
