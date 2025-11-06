package com.pedix.api.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.pedix.api.domain.enums.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "PEDIDO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pedido_seq_gen")
    @SequenceGenerator(name = "pedido_seq_gen", sequenceName = "PEDIDO_SEQ", allocationSize = 1)
    private Long id;

    @NotNull(message = "O ID da comanda é obrigatório.")
    @Column(name = "ID_COMANDA", nullable = false)
    private Long comandaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 50, nullable = false)
    private StatusPedido status = StatusPedido.EM_PREPARO;

    @Column(name = "OBSERVACAO", length = 500)
    private String observacao;

    @Column(name = "TOTAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "DATA_HORA", updatable = false)
    private LocalDateTime dataHora;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PedidoItem> itens = new ArrayList<>();


    public void adicionarItem(PedidoItem item) {
        if (item == null) return;
        item.setPedido(this);
        this.itens.add(item);
        recalcularTotal();
    }

    public void removerItem(PedidoItem item) {
        if (item == null) return;
        item.setPedido(null);
        this.itens.remove(item);
        recalcularTotal();
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        if (novoStatus != null) {
            this.status = novoStatus;
        }
    }


    @PrePersist
    @PreUpdate
    public void recalcularTotal() {
        this.total = this.itens.stream()
                .map(PedidoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return String.format("Pedido{id=%d, comandaId=%d, status=%s, total=%.2f, itens=%d}",
                id, comandaId, status, total, itens.size());
    }
}
