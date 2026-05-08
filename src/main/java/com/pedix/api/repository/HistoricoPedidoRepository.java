package com.pedix.api.repository;

import com.pedix.api.domain.HistoricoPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoPedidoRepository extends JpaRepository<HistoricoPedido, Long> {

    List<HistoricoPedido> findByPedidoId(Long pedidoId);

    List<HistoricoPedido> findByStatusNovo(String statusNovo);

    List<HistoricoPedido> findByUsuarioIgnoreCase(String usuario);
}