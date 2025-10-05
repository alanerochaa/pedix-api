package com.pedix.api;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.Pedido;
import com.pedix.api.domain.enums.CategoriaItem;
import com.pedix.api.domain.enums.StatusPedido;
import com.pedix.api.repository.ItemCardapioRepository;
import com.pedix.api.repository.PedidoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initData(ItemCardapioRepository itemRepo, PedidoRepository pedidoRepo) {
        return args -> {
            // Itens do cardápio
            ItemCardapio pizza = new ItemCardapio(null, "Pizza Calabresa", "Deliciosa pizza com calabresa", new BigDecimal("35.00"), CategoriaItem.PRATO, true, null);
            ItemCardapio refri = new ItemCardapio(null, "Refrigerante", "Coca Cola 350ml", new BigDecimal("8.50"), CategoriaItem.BEBIDA, true, null);
            ItemCardapio sorvete = new ItemCardapio(null, "Sorvete Chocolate", "Sobremesa gelada", new BigDecimal("12.00"), CategoriaItem.SOBREMESA, true, null);

            itemRepo.save(pizza);
            itemRepo.save(refri);
            itemRepo.save(sorvete);

            // Pedidos
            pedidoRepo.save(new Pedido(null, 1001L, pizza, 1, StatusPedido.EM_PREPARO, "Sem queijo ralado", LocalDateTime.now()));
            pedidoRepo.save(new Pedido(null, 1001L, refri, 2, StatusPedido.EM_PREPARO, "Um com gelo, outro sem", LocalDateTime.now()));
            pedidoRepo.save(new Pedido(null, 1002L, sorvete, 1, StatusPedido.ENTREGUE, "Entrega prioridade", LocalDateTime.now().minusHours(2)));
        };
    }
}
