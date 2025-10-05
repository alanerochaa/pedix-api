package com.pedix.api.repository;

import com.pedix.api.domain.ItemCardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {
    List<ItemCardapio> findByDisponivelTrue();
}
