package com.pedix.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoPedidoDTO {

    private Long id;
    private Long pedidoId;
    private String statusAnterior;
    private String statusNovo;
    private String descricao;
    private LocalDateTime dataRegistro;
    private String usuario;
}