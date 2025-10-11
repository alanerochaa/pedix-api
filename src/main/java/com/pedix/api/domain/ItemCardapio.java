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
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemCardapio {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_seq_gen")
    @SequenceGenerator(name = "item_seq_gen", sequenceName = "ITEM_CARDAPIO_SEQ", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CategoriaItem categoria;

    @NotNull
    @Positive
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean disponivel = true;

    // Métodos utilitários para facilitar manutenção
    public void ativar() { this.disponivel = true; }

    public void desativar() { this.disponivel = false; }

    @Override
    public String toString() {
        return "ItemCardapio{id=" + id + ", nome='" + nome + "', preco=" + preco + "}";
    }
}
