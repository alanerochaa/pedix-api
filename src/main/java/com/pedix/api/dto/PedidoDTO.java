// src/main/java/com/pedix/api/dto/PedidoDTO.java
package com.pedix.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "O pedido deve conter ao menos um item.")
    @Valid
    @Builder.Default
    private List<PedidoItemDTO> itens = new ArrayList<>();

    private String observacao;
}