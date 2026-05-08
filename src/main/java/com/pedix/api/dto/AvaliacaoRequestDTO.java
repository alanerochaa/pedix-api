package com.pedix.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoRequestDTO {

    private Long pedidoId;
    private Long itemCardapioId;
    private String nomeCliente;
    private Integer nota;
    private String comentario;
}