-- =====================================================
-- CONSULTAS DE VALIDAÇÃO - PEDIX API JAVA | SPRINT 4
-- =====================================================

SELECT * FROM categoria_cardapio;

SELECT * FROM item_cardapio;

SELECT * FROM pedido;

SELECT * FROM pedido_item;

SELECT * FROM avaliacao;

SELECT * FROM historico_pedido;

SELECT * FROM relatorio;

-- Validação de relacionamento entre item e categoria
SELECT
    i.id,
    i.nome AS item,
    c.nome AS categoria,
    i.preco,
    i.disponivel
FROM item_cardapio i
INNER JOIN categoria_cardapio c
    ON i.categoria_id = c.id;

-- Validação de avaliações por item
SELECT
    a.id,
    a.nome_cliente,
    a.nota,
    a.comentario,
    i.nome AS item_avaliado
FROM avaliacao a
INNER JOIN item_cardapio i
    ON a.item_cardapio_id = i.id;

-- Validação de histórico por pedido
SELECT
    h.id,
    h.pedido_id,
    h.status_anterior,
    h.status_novo,
    h.descricao,
    h.usuario,
    h.data_registro
FROM historico_pedido h
ORDER BY h.data_registro DESC;

-- Validação de relatórios gerenciais
SELECT
    tipo,
    titulo,
    valor_total,
    quantidade,
    responsavel,
    data_geracao
FROM relatorio;