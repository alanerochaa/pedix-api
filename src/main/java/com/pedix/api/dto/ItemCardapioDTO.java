package com.pedix.api.dto;

import com.pedix.api.domain.enums.CategoriaItem;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemCardapioDTO {
    @NotBlank private String nome;
    private String descricao;
    @NotNull @DecimalMin("0.01") private BigDecimal preco;
    @NotNull private CategoriaItem categoria;
    private Boolean disponivel;
    private String imagemUrl;
}
