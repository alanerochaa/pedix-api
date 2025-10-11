package com.pedix.api.controller;

import com.pedix.api.domain.ItemCardapio;
import com.pedix.api.domain.enums.CategoriaItem;
import com.pedix.api.dto.ItemCardapioDTO;
import com.pedix.api.service.ItemCardapioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/item-cardapio")
@RequiredArgsConstructor
@Tag(
        name = "🍔 Cardápio ",
        description = """
        Controla os **itens do cardápio** do restaurante.  
        Permite **criar**, **listar**, **buscar**, **atualizar** e **remover** pratos, bebidas e sobremesas.
        """
)
public class ItemCardapioController {

    private final ItemCardapioService service;


    // LISTAR ITENS DO CARDÁPIO

    @Operation(
            summary = "📋 Listar itens do cardápio",
            description = """
            Retorna todos os itens disponíveis no cardápio.  
            É possível **filtrar por categoria**, como 🍕 *PRATO*, 🥤 *BEBIDA* ou 🍰 *SOBREMESA*.
            """,
            parameters = {
                    @Parameter(
                            name = "categoria",
                            description = "Categoria do item (opcional): 🍕 PRATO, 🥤 BEBIDA ou 🍰 SOBREMESA.",
                            example = "PRATO",
                            schema = @Schema(implementation = CategoriaItem.class)
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Itens retornados com sucesso",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ItemCardapio.class))
                    )
            }
    )
    @GetMapping
    public List<ItemCardapio> listar(@RequestParam(required = false) CategoriaItem categoria) {
        if (categoria != null) return service.listarPorCategoria(categoria);
        return service.listarDisponiveis();
    }

    // BUSCAR ITEM POR ID
    @Operation(
            summary = "🔍 Buscar item por ID",
            description = """
            Retorna os detalhes completos de um item do cardápio com base no ID informado.  
            Ideal para verificar informações individuais de um produto.
            """,
            parameters = {
                    @Parameter(name = "id", description = "ID do item", example = "11")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ItemCardapio> buscarPorId(@PathVariable Long id) {
        ItemCardapio item = service.buscarPorId(id);
        return ResponseEntity.ok(item);
    }

    // CRIAR NOVO ITEM
    @Operation(
            summary = "➕ Criar novo item",
            description = """
            Adiciona um novo item ao cardápio.  
            Todos os campos obrigatórios devem ser preenchidos corretamente.
            """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do novo item de cardápio",
                    content = @Content(schema = @Schema(implementation = ItemCardapioDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Item criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados")
            }
    )
    @PostMapping
    public ResponseEntity<ItemCardapio> criar(
            @Valid @RequestBody ItemCardapioDTO dto,
            UriComponentsBuilder uriBuilder) {

        ItemCardapio salvo = service.criar(dto);
        URI location = uriBuilder.path("/api/item-cardapio/{id}")
                .buildAndExpand(salvo.getId())
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    // ️ ATUALIZAR ITEM EXISTENTE
    @Operation(
            summary = "✏️ Atualizar item",
            description = """
            Atualiza os dados de um item já existente no cardápio.  
            O ID do item deve ser informado na URL.
            """,
            parameters = {
                    @Parameter(name = "id", description = "ID do item a ser atualizado", example = "1")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ItemCardapio> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemCardapioDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // REMOVER ITEM
    @Operation(
            summary = "🗑️ Remover item",
            description = """
            Exclui um item do cardápio com base no ID informado.  
            ⚠️ **A operação é irreversível.**
            """,
            parameters = {
                    @Parameter(name = "id", description = "ID do item a ser excluído", example = "3")
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Item excluído com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Item não encontrado")
            }
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
