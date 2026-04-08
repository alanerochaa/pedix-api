package com.pedix.api.controller.web;

import com.pedix.api.dto.PedidoDTO;
import com.pedix.api.dto.PedidoItemDTO;
import com.pedix.api.dto.PedidoResponseDTO;
import com.pedix.api.service.ItemCardapioService;
import com.pedix.api.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoWebController {

    private final PedidoService pedidoService;
    private final ItemCardapioService itemCardapioService;

    @GetMapping
    public String listar(Authentication authentication, Model model) {
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        List<PedidoResponseDTO> pedidos = isAdmin
                ? pedidoService.listarTodosResponse()
                : pedidoService.listarPorGarcomResponse(authentication.getName());

        model.addAttribute("pedidos", pedidos);
        model.addAttribute("isAdmin", isAdmin);

        return "pedidos/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        var itensDisponiveis = itemCardapioService.listarDisponiveis();

        List<PedidoItemDTO> itens = new ArrayList<>(
                itensDisponiveis.stream()
                        .map(item -> PedidoItemDTO.builder()
                                .itemCardapioId(item.getId())
                                .quantidade(0)
                                .build())
                        .toList()
        );

        PedidoDTO dto = PedidoDTO.builder()
                .itens(itens)
                .build();

        model.addAttribute("pedido", dto);
        model.addAttribute("itensCardapio", itensDisponiveis);

        return "pedidos/form";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        PedidoResponseDTO pedido = pedidoService.buscarResponsePorId(id);
        model.addAttribute("pedido", pedido);
        return "pedidos/detalhe";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("pedido") PedidoDTO dto,
                         BindingResult bindingResult,
                         Authentication authentication,
                         Model model) {

        if (dto.getItens() != null) {
            dto.setItens(new ArrayList<>(dto.getItens()));
            dto.getItens().removeIf(item ->
                    item.getItemCardapioId() == null ||
                            item.getQuantidade() == null ||
                            item.getQuantidade() <= 0
            );
        }

        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            model.addAttribute("itensCardapio", itemCardapioService.listarDisponiveis());
            model.addAttribute("pedido", dto);
            model.addAttribute("erroItens", "Selecione ao menos um item com quantidade válida.");
            return "pedidos/form";
        }

        if (bindingResult.hasFieldErrors("comandaId")) {
            model.addAttribute("itensCardapio", itemCardapioService.listarDisponiveis());
            model.addAttribute("pedido", dto);
            return "pedidos/form";
        }

        pedidoService.criar(dto, authentication.getName());
        return "redirect:/pedidos";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelar(@PathVariable Long id) {
        pedidoService.cancelarPedido(id);
        return "redirect:/pedidos";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id) {
        pedidoService.excluir(id);
        return "redirect:/pedidos";
    }
}