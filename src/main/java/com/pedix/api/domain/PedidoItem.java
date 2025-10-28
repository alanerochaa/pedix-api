package com.pedix.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "PEDIDO_ITEM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PedidoItem {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_item_seq_gen")
    @SequenceGenerator(name = "pedido_item_seq_gen", sequenceName = "PEDIDO_ITEM_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PEDIDO_ID", nullable = false)
    @JsonBackReference
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ITEM_CARDAPIO_ID", nullable = false)
    private ItemCardapio itemCardapio;

    @NotNull
    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "PRECO_UNITARIO", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoUnitario;

    @Column(name = "SUBTOTAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal subtotal;

    public void definirPrecoPadrao() {
        if (itemCardapio != null && itemCardapio.getPreco() != null) {
            this.precoUnitario = itemCardapio.getPreco();
            this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }
}
