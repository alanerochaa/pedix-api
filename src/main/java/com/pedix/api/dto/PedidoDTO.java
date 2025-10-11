package com.pedix.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
    private List<PedidoItemDTO> itens;
}
