package com.pedix.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HISTORICO_PEDIDO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HistoricoPedido {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historico_pedido_seq_gen")
    @SequenceGenerator(
            name = "historico_pedido_seq_gen",
            sequenceName = "HISTORICO_PEDIDO_SEQ",
            allocationSize = 1
    )
    private Long id;

    @NotNull(message = "O pedido é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEDIDO_ID", nullable = false)
    private Pedido pedido;

    @Column(name = "STATUS_ANTERIOR", length = 50)
    private String statusAnterior;

    @NotNull(message = "O novo status é obrigatório.")
    @Column(name = "STATUS_NOVO", nullable = false, length = 50)
    private String statusNovo;

    @Column(length = 500)
    private String descricao;

    @Column(name = "DATA_REGISTRO", nullable = false)
    private LocalDateTime dataRegistro;

    @Column(length = 150)
    private String usuario;

    @PrePersist
    public void prePersist() {
        if (dataRegistro == null) {
            dataRegistro = LocalDateTime.now();
        }
    }
}