-- ====================================================
-- V2__insert_data.sql
-- Carga inicial de dados do Pedix
-- API Java - Secundaria/Suporte e Gestao
-- ====================================================

INSERT INTO categoria_cardapio (nome, descricao, ativo)
VALUES ('PRATO', 'Pratos principais do cardapio.', 1);

INSERT INTO categoria_cardapio (nome, descricao, ativo)
VALUES ('BEBIDA', 'Bebidas disponiveis para consumo.', 1);

INSERT INTO categoria_cardapio (nome, descricao, ativo)
VALUES ('SOBREMESA', 'Sobremesas oferecidas pelo restaurante.', 1);

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Pizza Calabresa', 'Pizza com molho de tomate, mussarela e calabresa fatiada.', 35.00, 1, NULL);

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (2, 'Refrigerante Cola 350ml', 'Bebida gaseificada gelada.', 8.50, 1, NULL);

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (3, 'Sorvete de Chocolate', 'Sobremesa gelada sabor chocolate.', 12.00, 1, NULL);

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Hamburguer Artesanal', 'Pao brioche, carne artesanal, queijo e molho especial.', 32.90, 1, NULL);

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (2, 'Suco de Laranja 300ml', 'Suco natural sem conservantes.', 7.50, 1, NULL);

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1001, 'EM_PREPARO', 'Sem cebola.', 35.00, 'garcom');

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1002, 'PRONTO', 'Um com gelo, outro sem.', 17.00, 'garcom');

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1003, 'ENTREGUE', 'Entrega rapida na mesa 5.', 12.00, 'admin');

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1004, 'FINALIZADO', 'Pedido finalizado sem intercorrencias.', 32.90, 'admin');

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1005, 'CANCELADO', 'Cliente desistiu do pedido.', 7.50, 'garcom');

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (1, 1, 1, 35.00, 35.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (2, 2, 2, 8.50, 17.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (3, 3, 1, 12.00, 12.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (4, 4, 1, 32.90, 32.90);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (5, 5, 1, 7.50, 7.50);

INSERT INTO avaliacao (pedido_id, item_cardapio_id, nome_cliente, nota, comentario)
VALUES (1, 1, 'Ana Souza', 5, 'Pizza muito boa e chegou quente.');

INSERT INTO avaliacao (pedido_id, item_cardapio_id, nome_cliente, nota, comentario)
VALUES (2, 2, 'Bruno Lima', 4, 'Bebida gelada e atendimento rapido.');

INSERT INTO avaliacao (pedido_id, item_cardapio_id, nome_cliente, nota, comentario)
VALUES (3, 3, 'Carla Mendes', 5, 'Sobremesa excelente.');

INSERT INTO historico_pedido (pedido_id, status_anterior, status_novo, descricao, usuario)
VALUES (1, NULL, 'EM_PREPARO', 'Pedido criado e enviado para preparo.', 'garcom');

INSERT INTO historico_pedido (pedido_id, status_anterior, status_novo, descricao, usuario)
VALUES (2, 'EM_PREPARO', 'PRONTO', 'Pedido finalizado pela cozinha.', 'admin');

INSERT INTO historico_pedido (pedido_id, status_anterior, status_novo, descricao, usuario)
VALUES (3, 'PRONTO', 'ENTREGUE', 'Pedido entregue ao cliente.', 'garcom');

INSERT INTO historico_pedido (pedido_id, status_anterior, status_novo, descricao, usuario)
VALUES (4, 'ENTREGUE', 'FINALIZADO', 'Pedido finalizado na comanda.', 'admin');

INSERT INTO historico_pedido (pedido_id, status_anterior, status_novo, descricao, usuario)
VALUES (5, 'EM_PREPARO', 'CANCELADO', 'Pedido cancelado por solicitacao do cliente.', 'garcom');

INSERT INTO relatorio (tipo, titulo, descricao, valor_total, quantidade, responsavel)
VALUES ('VENDAS', 'Relatorio de vendas do dia', 'Resumo administrativo com total vendido nos pedidos cadastrados.', 104.40, 5, 'admin');

INSERT INTO relatorio (tipo, titulo, descricao, valor_total, quantidade, responsavel)
VALUES ('CARDAPIO', 'Relatorio de itens ativos', 'Quantidade de itens disponiveis no cardapio.', NULL, 5, 'admin');

INSERT INTO relatorio (tipo, titulo, descricao, valor_total, quantidade, responsavel)
VALUES ('AVALIACOES', 'Relatorio de avaliacoes', 'Quantidade de avaliacoes registradas pelos clientes.', NULL, 3, 'admin');

COMMIT;