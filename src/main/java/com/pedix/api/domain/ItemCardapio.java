package com.pedix.api.domain;

import com.pedix.api.domain.enums.CategoriaItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "item_cardapio")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ItemCardapio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @NotNull @DecimalMin("0.01")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CategoriaItem categoria;

    @NotNull
    @Column(nullable = false)
    private Boolean disponivel = true;

    @Column(name = "imagem_url", length = 500)
    private String imagemUrl;
}
