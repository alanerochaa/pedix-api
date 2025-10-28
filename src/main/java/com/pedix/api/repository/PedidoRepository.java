package com.pedix.api.repository;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Retorna todos os pedidos de uma comanda
    List<Pedido> findByComandaId(Long comandaId);

    // Retorna todos os pedidos com um status específico
    List<Pedido> findByStatus(StatusPedido status);

    // Exemplo de JPQL personalizada — pedidos filtrados por status e observação
    @Query("SELECT p FROM Pedido p WHERE p.status = :status AND LOWER(p.observacao) LIKE LOWER(CONCAT('%', :observacao, '%'))")
    List<Pedido> buscarPorStatusEObservacao(@Param("status") StatusPedido status,
                                            @Param("observacao") String observacao);
}
