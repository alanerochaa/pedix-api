package com.pedix.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoItemDTO {
    @NotNull private Long itemId;
    @NotNull @Min(1) private Integer quantidade;
}
