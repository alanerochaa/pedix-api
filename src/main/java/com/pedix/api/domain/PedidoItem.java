package com.pedix.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_item")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PedidoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_cardapio_id", nullable = false)
    private ItemCardapio item;

    @NotNull @Min(1)
    @Column(nullable = false)
    private Integer quantidade;

    // preço copiado do ItemCardapio no momento do pedido
    @NotNull
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @NotNull
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @PrePersist @PreUpdate
    public void prePersist() {
        if (precoUnitario == null && item != null) {
            precoUnitario = item.getPreco();
        }
        subtotal = precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
