package com.pedix.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {

    @NotEmpty(message = "O pedido deve conter ao menos um item.")
    @Valid
    private List<PedidoItemDTO> itens;

    private String observacao;
}
