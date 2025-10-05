package com.pedix.api.dto;

import lombok.Data;

@Data
public class PedidoDTO {
    private Long comandaId;
    private Long itemId;
    private Integer quantidade;
    private String observacao;
}
