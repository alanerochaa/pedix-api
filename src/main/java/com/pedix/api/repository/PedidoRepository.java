// src/main/java/com/pedix/api/repository/PedidoRepository.java
package com.pedix.api.repository;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByComandaId(Long comandaId);

    List<Pedido> findByStatus(StatusPedido status);

    List<Pedido> findByGarcomResponsavel(String garcomResponsavel);

    @Query("""
        SELECT p FROM Pedido p
        WHERE p.status = :status
        AND LOWER(COALESCE(p.observacao, '')) LIKE LOWER(CONCAT('%', :observacao, '%'))
    """)
    List<Pedido> buscarPorStatusEObservacao(@Param("status") StatusPedido status,
                                            @Param("observacao") String observacao);
}