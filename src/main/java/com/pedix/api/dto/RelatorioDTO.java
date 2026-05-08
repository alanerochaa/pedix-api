package com.pedix.api.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RelatorioDTO {

    private Long id;
    private String tipo;
    private String titulo;
    private String descricao;
    private BigDecimal valorTotal;
    private Integer quantidade;
    private LocalDateTime dataGeracao;
    private String responsavel;
}