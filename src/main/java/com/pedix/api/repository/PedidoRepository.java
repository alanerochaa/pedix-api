package com.pedix.api.repository;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByComandaId(Long comandaId);

    List<Pedido> findByStatus(StatusPedido status);
}
