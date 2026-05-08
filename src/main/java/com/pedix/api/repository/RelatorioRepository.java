package com.pedix.api.repository;

import com.pedix.api.domain.Relatorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

    List<Relatorio> findByTipoIgnoreCase(String tipo);

    List<Relatorio> findByResponsavelIgnoreCase(String responsavel);
}