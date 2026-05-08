package com.pedix.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoDTO {

    private Long id;
    private Long pedidoId;
    private Long itemCardapioId;
    private String nomeCliente;
    private Integer nota;
    private String comentario;
    private LocalDateTime dataAvaliacao;
}