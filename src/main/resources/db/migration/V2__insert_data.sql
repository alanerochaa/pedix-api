-- ====================================================
--V2__insert_data.sql
-- Carga inicial de dados do Pedix
-- ====================================================

INSERT INTO item_cardapio (nome, descricao, categoria, preco, disponivel, imagem_url)
VALUES ('Pizza Calabresa', 'Pizza com molho de tomate, mussarela e calabresa fatiada.', 'PRATO', 35.00, 1, NULL);

INSERT INTO item_cardapio (nome, descricao, categoria, preco, disponivel, imagem_url)
VALUES ('Refrigerante Cola 350ml', 'Bebida gaseificada gelada.', 'BEBIDA', 8.50, 1, NULL);

INSERT INTO item_cardapio (nome, descricao, categoria, preco, disponivel, imagem_url)
VALUES ('Sorvete de Chocolate', 'Sobremesa gelada sabor chocolate.', 'SOBREMESA', 12.00, 1, NULL);

INSERT INTO item_cardapio (nome, descricao, categoria, preco, disponivel, imagem_url)
VALUES ('Hamburguer Artesanal', 'Pao brioche, carne artesanal, queijo e molho especial.', 'PRATO', 32.90, 1, NULL);

INSERT INTO item_cardapio (nome, descricao, categoria, preco, disponivel, imagem_url)
VALUES ('Suco de Laranja 300ml', 'Suco natural sem conservantes.', 'BEBIDA', 7.50, 1, NULL);

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

COMMIT;