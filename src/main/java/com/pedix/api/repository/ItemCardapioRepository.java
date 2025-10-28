package com.pedix.api.repository;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.enums.CategoriaItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {

    // Retorna todos os itens disponíveis
    List<ItemCardapio> findByDisponivelTrue();

    // Retorna itens disponíveis paginados
    Page<ItemCardapio> findByDisponivelTrue(Pageable pageable);

    // Filtra por categoria e apenas itens disponíveis
    List<ItemCardapio> findByCategoriaAndDisponivelTrue(CategoriaItem categoria);

    // Exemplo de consulta JPQL personalizada (para demonstrar domínio)
    @Query("SELECT i FROM ItemCardapio i WHERE LOWER(i.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND i.disponivel = true")
    List<ItemCardapio> buscarPorNome(@Param("nome") String nome);
}
