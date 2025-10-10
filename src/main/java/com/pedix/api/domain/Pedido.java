package com.pedix.api.domain;

import com.pedix.api.domain.enums.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referência à Comanda (vinda da API C#)
    @NotNull
    @Column(name = "id_comanda", nullable = false)
    private Long comandaId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusPedido status = StatusPedido.EM_PREPARO;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(length = 500)
    private String observacao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PedidoItem> itens = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (dataHora == null) dataHora = LocalDateTime.now();
        if (status == null) status = StatusPedido.EM_PREPARO;
        recalcTotal();
    }

    public void addItem(PedidoItem item) {
        item.setPedido(this);
        itens.add(item);
        recalcTotal();
    }

    public void recalcTotal() {
        total = itens.stream()
                .map(PedidoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
