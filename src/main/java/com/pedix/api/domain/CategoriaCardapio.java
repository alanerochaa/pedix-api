package com.pedix.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "CATEGORIA_CARDAPIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CategoriaCardapio {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_seq_gen")
    @SequenceGenerator(
            name = "categoria_seq_gen",
            sequenceName = "CATEGORIA_CARDAPIO_SEQ",
            allocationSize = 1
    )
    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Column(nullable = false, length = 100, unique = true)
    private String nome;

    @Column(length = 300)
    private String descricao;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = true;

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }

    public void atualizarInformacoes(String nome, String descricao, Boolean ativo) {
        if (nome != null && !nome.isBlank()) {
            this.nome = nome;
        }

        this.descricao = descricao;

        if (ativo != null) {
            this.ativo = ativo;
        }
    }
}