package com.pedix.api.dto;

import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoItemResponseDTO {
    private Long id;
    private Long pedidoId;
    private Long itemCardapioId;
    private String nomeItem;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
}
