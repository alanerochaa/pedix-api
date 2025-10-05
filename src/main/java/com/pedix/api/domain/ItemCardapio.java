package com.pedix.api.domain;

import com.pedix.api.domain.enums.CategoriaItem;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@Slf4j
@AllArgsConstructor
@Entity
@Table(name = "item_cardapio")
public class ItemCardapio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    private CategoriaItem categoria;

    private Boolean disponivel = true;

    private String imagemUrl;

}
