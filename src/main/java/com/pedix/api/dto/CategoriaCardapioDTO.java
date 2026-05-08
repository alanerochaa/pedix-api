package com.pedix.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaCardapioDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;
}