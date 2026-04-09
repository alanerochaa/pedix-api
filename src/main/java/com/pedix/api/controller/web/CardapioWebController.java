package com.pedix.api.controller.web;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.dto.ItemCardapioDTO;
import com.pedix.api.service.ItemCardapioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cardapio")
@RequiredArgsConstructor
public class CardapioWebController {

    private final ItemCardapioService itemCardapioService;

    @GetMapping
    public String listar(@RequestParam(required = false) String busca, Model model) {
        if (busca != null && !busca.trim().isEmpty()) {
            model.addAttribute("itens", itemCardapioService.buscarDisponiveisPorNome(busca));
        } else {
            model.addAttribute("itens", itemCardapioService.listarDisponiveis());
        }

        model.addAttribute("busca", busca);
        return "cardapio/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("item", new ItemCardapioDTO());
        model.addAttribute("modoEdicao", false);
        model.addAttribute("formAction", "/cardapio/salvar");
        return "cardapio/form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        ItemCardapio item = itemCardapioService.buscarPorId(id);

        ItemCardapioDTO dto = ItemCardapioDTO.builder()
                .nome(item.getNome())
                .descricao(item.getDescricao())
                .categoria(item.getCategoria())
                .preco(item.getPreco())
                .disponivel(item.getDisponivel())
                .imagemUrl(item.getImagemUrl())
                .build();

        model.addAttribute("item", dto);
        model.addAttribute("modoEdicao", true);
        model.addAttribute("formAction", "/cardapio/atualizar/" + id);

        return "cardapio/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("item") ItemCardapioDTO dto,
                         BindingResult result,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("modoEdicao", false);
            model.addAttribute("formAction", "/cardapio/salvar");
            return "cardapio/form";
        }

        itemCardapioService.criar(dto);
        return "redirect:/cardapio";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid @ModelAttribute("item") ItemCardapioDTO dto,
                            BindingResult result,
                            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("modoEdicao", true);
            model.addAttribute("formAction", "/cardapio/atualizar/" + id);
            return "cardapio/form";
        }

        itemCardapioService.atualizar(id, dto);
        return "redirect:/cardapio";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        itemCardapioService.excluir(id);
        return "redirect:/cardapio";
    }
}