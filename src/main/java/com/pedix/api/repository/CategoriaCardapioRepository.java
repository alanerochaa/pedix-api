package com.pedix.api.repository;

import com.pedix.api.domain.CategoriaCardapio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaCardapioRepository extends JpaRepository<CategoriaCardapio, Long> {

    Optional<CategoriaCardapio> findByNomeIgnoreCase(String nome);

    List<CategoriaCardapio> findByAtivoTrue();

    boolean existsByNomeIgnoreCase(String nome);
}