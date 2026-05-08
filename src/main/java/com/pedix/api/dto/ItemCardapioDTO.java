package com.pedix.api.dto;

import com.pedix.api.domain.ItemCardapio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCardapioDTO {

    private Long id;

    @NotBlank(message = "O nome do item é obrigatório.")
    @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres.")
    private String nome;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    private String descricao;

    @NotNull(message = "A categoria do item é obrigatória.")
    private Long categoriaId;

    private String categoriaNome;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    private BigDecimal preco;

    private Boolean disponivel = true;

    @Size(max = 500, message = "A URL da imagem deve ter no máximo 500 caracteres.")
    private String imagemUrl;

    public static ItemCardapioDTO fromEntity(ItemCardapio item) {
        return ItemCardapioDTO.builder()
                .id(item.getId())
                .nome(item.getNome())
                .descricao(item.getDescricao())
                .categoriaId(
                        item.getCategoria() != null
                                ? item.getCategoria().getId()
                                : null
                )
                .categoriaNome(
                        item.getCategoria() != null
                                ? item.getCategoria().getNome()
                                : null
                )
                .preco(item.getPreco())
                .disponivel(item.getDisponivel())
                .imagemUrl(item.getImagemUrl())
                .build();
    }
}