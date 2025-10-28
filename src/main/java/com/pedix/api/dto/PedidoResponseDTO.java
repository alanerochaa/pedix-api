package com.pedix.api.dto;

import com.pedix.api.domain.enums.StatusPedido;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {

    private Long id;
    private Long comandaId;
    private StatusPedido status;
    private LocalDateTime dataCriacao;

    private String observacao;

    private BigDecimal total;
    private List<ItemResumo> itens;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ItemResumo {
        private Long itemCardapioId;
        private String nome;
        private Integer quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal subtotal;
    }
}
