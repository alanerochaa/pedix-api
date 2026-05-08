package com.pedix.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RELATORIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Relatorio {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "relatorio_seq_gen")
    @SequenceGenerator(
            name = "relatorio_seq_gen",
            sequenceName = "RELATORIO_SEQ",
            allocationSize = 1
    )
    private Long id;

    @NotBlank(message = "O tipo do relatório é obrigatório.")
    @Column(nullable = false, length = 80)
    private String tipo;

    @NotBlank(message = "O título do relatório é obrigatório.")
    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 500)
    private String descricao;

    @PositiveOrZero(message = "O valor total não pode ser negativo.")
    @Column(name = "VALOR_TOTAL", precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @PositiveOrZero(message = "A quantidade não pode ser negativa.")
    private Integer quantidade;

    @Column(name = "DATA_GERACAO", nullable = false)
    private LocalDateTime dataGeracao;

    @Column(length = 150)
    private String responsavel;

    @PrePersist
    public void prePersist() {
        if (dataGeracao == null) {
            dataGeracao = LocalDateTime.now();
        }
    }
}