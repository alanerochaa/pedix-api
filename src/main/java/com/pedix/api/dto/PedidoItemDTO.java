package com.pedix.api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoItemDTO {

    private Long itemCardapioId;
    private Integer quantidade;
}