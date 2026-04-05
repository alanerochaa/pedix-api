// src/main/java/com/pedix/api/domain/ItemCardapio.java
package com.pedix.api.domain;

import com.pedix.api.domain.enums.CategoriaItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ITEM_CARDAPIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemCardapio {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
    @SequenceGenerator(name = "item_seq_gen", sequenceName = "ITEM_CARDAPIO_SEQ", allocationSize = 1)
    private Long id;

    @NotBlank(message = "O nome do item é obrigatório.")
    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "A categoria do item é obrigatória.")
    @Column(nullable = false, length = 30)
    private CategoriaItem categoria;

    @NotNull(message = "O preço é obrigatório.")
    @Positive(message = "O preço deve ser maior que zero.")
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal preco;

    @NotNull
    @Column(nullable = false)
    private Boolean disponivel = true;

    @Column(name = "IMAGEM_URL", length = 500)
    private String imagemUrl;

    public void ativar() {
        this.disponivel = true;
    }

    public void desativar() {
        this.disponivel = false;
    }

    public void atualizarInformacoes(
            String nome,
            String descricao,
            CategoriaItem categoria,
            BigDecimal preco,
            Boolean disponivel,
            String imagemUrl
    ) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }
        this.descricao = descricao;
        if (categoria != null) {
            this.categoria = categoria;
        }
        if (preco != null && preco.compareTo(BigDecimal.ZERO) > 0) {
            this.preco = preco;
        }
        if (disponivel != null) {
            this.disponivel = disponivel;
        }
        this.imagemUrl = imagemUrl;
    }

    @Override
    public String toString() {
        return String.format(
                "ItemCardapio[id=%d, nome='%s', preco=%s, categoria=%s, disponivel=%s]",
                id, nome, preco, categoria, disponivel
        );
    }
}