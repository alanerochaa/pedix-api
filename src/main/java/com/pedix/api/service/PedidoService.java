package com.pedix.api.service;

import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    public Pedido criarPedido(Pedido pedido) {
        pedido.setDataHora(LocalDateTime.now());
        pedido.setStatus(StatusPedido.EM_PREPARO);
        return repository.save(pedido);
    }

    public List<Pedido> listarPedidosPorComanda(Long comandaId) {
        return repository.findByComandaId(comandaId);
    }

    public Pedido atualizarStatus(Long id, StatusPedido status) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(status);
        return repository.save(pedido);
    }
}
