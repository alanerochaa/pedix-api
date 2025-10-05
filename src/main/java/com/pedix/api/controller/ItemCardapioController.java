package com.pedix.api.controller;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.service.ItemCardapioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/item-cardapio")
public class ItemCardapioController {

    @Autowired
    private ItemCardapioService service;

    @GetMapping
    public List<ItemCardapio> listar() {
        return service.listarDisponiveis();
    }

    @PostMapping
    public ItemCardapio criar(@RequestBody ItemCardapio item) {
        return service.salvar(item);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
