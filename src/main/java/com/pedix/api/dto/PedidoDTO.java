package com.pedix.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class PedidoDTO {
    private String observacao;
    @NotEmpty
    private List<PedidoItemDTO> itens;
}
