package com.pedix.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {

    @NotNull(message = "O ID da comanda é obrigatório.")
    private Long comandaId;

    @Builder.Default
    private List<PedidoItemDTO> itens = new ArrayList<>();

    private String observacao;
}