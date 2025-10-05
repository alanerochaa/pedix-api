package com.pedix.api.service;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.repository.ItemCardapioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCardapioService {

    @Autowired
    private ItemCardapioRepository repository;

    public List<ItemCardapio> listarDisponiveis() {
        return repository.findByDisponivelTrue();
    }

    public ItemCardapio salvar(ItemCardapio item) {
        return repository.save(item);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
