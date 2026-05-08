package com.pedix.api.repository;

import com.pedix.api.domain.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {

    List<Avaliacao> findByPedidoId(Long pedidoId);

    List<Avaliacao> findByItemCardapioId(Long itemCardapioId);

    List<Avaliacao> findByNota(Integer nota);
}