package com.pedix.api.domain;

import com.pedix.api.domain.enums.StatusPedido;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Slf4j
@AllArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_comanda", nullable = false)
    private Long comandaId; // Integração com API C#

    @ManyToOne
    @JoinColumn(name = "id_item", nullable = false)
    private ItemCardapio item;

    @Column(nullable = false)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private String observacao;

    private LocalDateTime dataHora;

}
