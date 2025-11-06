package com.pedix.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;


@Entity
@Table(name = "PEDIDO_ITEM")
@Getter
@Setter
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
    @NotNull(message = "O item do cardápio é obrigatório.")
    private ItemCardapio itemCardapio;

    @NotNull(message = "A quantidade é obrigatória.")
    @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
    @Column(name = "QUANTIDADE", nullable = false)
    private Integer quantidade;

    @Column(name = "PRECO_UNITARIO", precision = 10, scale = 2, nullable = false)
    private BigDecimal precoUnitario = BigDecimal.ZERO;

    @Column(name = "SUBTOTAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal subtotal = BigDecimal.ZERO;

    public void definirPrecoPadrao() {
        if (itemCardapio != null && itemCardapio.getPreco() != null) {
            this.precoUnitario = itemCardapio.getPreco();
            this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }


    public void recalcularSubtotal() {
        if (precoUnitario != null && quantidade != null) {
            this.subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
        }
    }

    @Override
    public String toString() {
        return String.format("PedidoItem{id=%d, item='%s', qtd=%d, preco=%.2f, subtotal=%.2f}",
                id,
                itemCardapio != null ? itemCardapio.getNome() : "N/A",
                quantidade,
                precoUnitario,
                subtotal);
    }
}
