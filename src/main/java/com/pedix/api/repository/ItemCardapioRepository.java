// src/main/java/com/pedix/api/repository/ItemCardapioRepository.java
package com.pedix.api.repository;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.enums.CategoriaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {

    List<ItemCardapio> findByDisponivelTrue();

    List<ItemCardapio> findByDisponivelTrueAndNomeContainingIgnoreCase(String nome);

    List<ItemCardapio> findByDisponivelTrueAndCategoria(CategoriaItem categoria);
}