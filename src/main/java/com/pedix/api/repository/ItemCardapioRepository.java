package com.pedix.api.repository;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.enums.CategoriaItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {

    List<ItemCardapio> findByDisponivelTrue();

    Page<ItemCardapio> findByDisponivelTrue(Pageable pageable);

    List<ItemCardapio> findByCategoriaAndDisponivelTrue(CategoriaItem categoria);
}
