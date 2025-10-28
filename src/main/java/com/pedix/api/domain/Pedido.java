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

/**
 * Representa um pedido vinculado a uma comanda de restaurante.
 * Cada pedido possui status, itens associados e cálculo automático de total.
 */
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

    /** ID da comanda associada ao pedido (vinculado à API .NET) */
    @NotNull(message = "O ID da comanda é obrigatório.")
    @Column(name = "ID_COMANDA", nullable = false)
    private Long comandaId;

    /** Status do pedido — default: EM_PREPARO */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 50, nullable = false)
    private StatusPedido status = StatusPedido.EM_PREPARO;

    /** Observações adicionais do cliente */
    @Column(name = "OBSERVACAO", length = 500)
    private String observacao;

    /** Valor total do pedido calculado automaticamente */
    @Column(name = "TOTAL", precision = 12, scale = 2, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;

    /** Data/hora de criação do pedido (definida automaticamente) */
    @CreationTimestamp
    @Column(name = "DATA_HORA", updatable = false)
    private LocalDateTime dataHora;

    /** Itens vinculados ao pedido */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PedidoItem> itens = new ArrayList<>();

    // ====================== MÉTODOS DE DOMÍNIO ====================== //

    /**
     * Adiciona um item ao pedido e recalcula o total.
     */
    public void adicionarItem(PedidoItem item) {
        if (item == null) return;
        item.setPedido(this);
        this.itens.add(item);
        recalcularTotal();
    }

    /**
     * Remove um item do pedido e recalcula o total.
     */
    public void removerItem(PedidoItem item) {
        if (item == null) return;
        item.setPedido(null);
        this.itens.remove(item);
        recalcularTotal();
    }

    /**
     * Atualiza o status do pedido.
     */
    public void atualizarStatus(StatusPedido novoStatus) {
        if (novoStatus != null) {
            this.status = novoStatus;
        }
    }

    /**
     * Recalcula o valor total com base nos itens.
     */
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
