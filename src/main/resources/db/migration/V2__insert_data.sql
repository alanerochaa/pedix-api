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

-- ====================================================
-- ITENS DO CARDAPIO
-- categoria_id: 1 = PRATO | 2 = BEBIDA | 3 = SOBREMESA
-- ====================================================

-- BEBIDAS

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (2, 'Agua', 'Agua mineral sem gas, garrafa 500ml. Tags: vegano, sem gluten, sem lactose.', 4.50, 1, 'https://images.unsplash.com/photo-1548839140-29a749e1cf4d?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (2, 'Refrigerante', 'Refrigerante gelado, lata 350ml. Sabores disponiveis: Coca-Cola, Coca Zero, Guarana Antarctica, Sprite e Fanta Laranja. Tags: vegano, sem gluten, sem lactose. Atencao: Coca-Cola e Guarana contem cafeina.', 6.00, 1, 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (2, 'Suco Natural', 'Suco natural da fruta, copo 300ml. Sabores disponiveis: laranja, maracuja, abacaxi com hortela e limao siciliano. Tags: vegano, sem gluten, sem lactose.', 8.00, 1, 'https://images.unsplash.com/photo-1600271886742-f049cd451bba?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (2, 'Espresso Italiano', 'Cafe espresso encorpado, servido na xicara tradicional. Tags: vegano, sem gluten, sem lactose, contem cafeina.', 7.00, 1, 'https://images.unsplash.com/photo-1510707577719-ae7c14805e3a?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (2, 'Limonada Siciliana', 'Limonada refrescante com hortela e limao siciliano. Tags: vegano, sem gluten, sem lactose.', 14.00, 1, 'https://images.unsplash.com/photo-1621263764928-df1444c5e859?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (2, 'Soda Italiana', 'Soda italiana artesanal e refrescante. Sabores: maca verde, frutas vermelhas e lichia. Tags: vegano, sem gluten, sem lactose.', 12.00, 1, 'https://images.unsplash.com/photo-1512482017241-ccce0181a7fd?w=800&q=80');

-- PRATOS

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Hamburguer Artesanal', 'Hamburguer artesanal com queijo, alface, tomate e molho especial no pao brioche. Tags: contem gluten, contem lactose.', 25.00, 1, 'https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Batata Frita', 'Porcao media de batata frita crocante com sal e oregano. Tags: vegano, sem gluten, sem lactose.', 15.00, 1, 'https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Pizza Margherita', 'Massa fina com molho de tomate, mussarela e manjericao fresco. Tags: vegetariano, contem gluten, contem lactose.', 35.00, 1, 'https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Insalata Caprese', 'Tomate, mussarela de bufala, manjericao e azeite extravirgem. Tags: vegetariano, sem gluten, contem lactose.', 28.00, 1, 'https://images.unsplash.com/photo-1592417817098-8fd3d9eb14a5?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Bruschetta Caprese', 'Pao italiano tostado com tomate, mussarela de bufala e manjericao. Tags: vegetariano, contem gluten, contem lactose.', 22.00, 1, 'https://images.unsplash.com/photo-1572695157366-5e585ab2b69f?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Arancini', 'Bolinhos crocantes de risoto recheados com mussarela e empanados. Tags: vegetariano, contem gluten, contem lactose, contem ovo.', 28.00, 1, 'https://images.unsplash.com/photo-1595295333158-4742f28fbd85?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Spaghetti Carbonara', 'Massa al dente com molho cremoso a base de gema, bacon crocante e parmesao. Tags: contem gluten, contem lactose, contem ovo.', 42.00, 1, 'https://images.unsplash.com/photo-1612874742237-6526221588e3?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Lasagna alla Bolognese', 'Camadas de massa fresca, ragu de carne, molho bechamel e parmesao gratinado no forno. Tags: contem gluten, contem lactose, contem ovo.', 48.00, 1, 'https://images.unsplash.com/photo-1574894709920-11b28e7367e3?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Risotto ai Funghi', 'Arroz arboreo cremoso com mix de cogumelos e parmesao. Tags: vegetariano, sem gluten, contem lactose.', 45.00, 1, 'https://images.unsplash.com/photo-1476124369491-e7addf5db371?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (1, 'Fettuccine Alfredo', 'Massa fresca ao molho cremoso de parmesao, manteiga e creme de leite. Tags: vegetariano, contem gluten, contem lactose, contem ovo.', 38.00, 1, 'https://images.unsplash.com/photo-1645112411341-6c4fd023714a?w=300');

-- SOBREMESAS

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (3, 'Sorvete Artesanal', 'Sorvete artesanal italiano, 2 bolas a escolha entre chocolate, baunilha, pistache, morango e limao siciliano. Tags: vegetariano, sem gluten, contem lactose.', 16.00, 1, 'https://images.unsplash.com/photo-1497034825429-c343d7c6a68f?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (3, 'Panna Cotta', 'Creme italiano de leite e baunilha com calda de frutas vermelhas. Tags: vegetariano, sem gluten, contem lactose.', 18.00, 1, 'https://images.unsplash.com/photo-1488477181946-6428a0291777?w=300');

INSERT INTO item_cardapio (categoria_id, nome, descricao, preco, disponivel, imagem_url)
VALUES (3, 'Tiramisu', 'Classico italiano com biscoito champagne embebido em cafe, creme de mascarpone e cacau em po. Tags: vegetariano, contem gluten, contem lactose, contem ovo, contem cafeina.', 24.00, 1, 'https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?w=300');

-- ====================================================
-- PEDIDOS DE APOIO
-- Mantidos na API Java apenas para demonstracao, avaliacoes,
-- historico e relatorios. Fluxo principal fica na API C#.
-- ====================================================

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1001, 'EM_PREPARO', 'Sem queijo ralado.', 35.00, 'garcom');

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1002, 'PRONTO', 'Um refrigerante com gelo e outro sem.', 12.00, 'garcom');

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1003, 'ENTREGUE', 'Sobremesa entregue na mesa 5.', 16.00, 'admin');

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1004, 'FINALIZADO', 'Pedido finalizado sem intercorrencias.', 25.00, 'admin');

INSERT INTO pedido (id_comanda, status, observacao, total, garcom_responsavel)
VALUES (1005, 'CANCELADO', 'Cliente desistiu do pedido antes do preparo.', 8.00, 'garcom');

-- ====================================================
-- ITENS DOS PEDIDOS DE APOIO
-- ====================================================

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (1, 9, 1, 35.00, 35.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (2, 2, 2, 6.00, 12.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (3, 17, 1, 16.00, 16.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (4, 7, 1, 25.00, 25.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (5, 3, 1, 8.00, 8.00);

-- ====================================================
-- AVALIACOES
-- ====================================================

INSERT INTO avaliacao (pedido_id, item_cardapio_id, nome_cliente, nota, comentario)
VALUES (1, 9, 'Ana Souza', 5, 'Pizza muito boa e chegou quente.');

INSERT INTO avaliacao (pedido_id, item_cardapio_id, nome_cliente, nota, comentario)
VALUES (2, 2, 'Bruno Lima', 4, 'Bebida gelada e atendimento rapido.');

INSERT INTO avaliacao (pedido_id, item_cardapio_id, nome_cliente, nota, comentario)
VALUES (3, 17, 'Carla Mendes', 5, 'Sobremesa excelente.');

-- ====================================================
-- HISTORICO DOS PEDIDOS
-- ====================================================

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

-- ====================================================
-- RELATORIOS
-- ====================================================

INSERT INTO relatorio (tipo, titulo, descricao, valor_total, quantidade, responsavel)
VALUES ('VENDAS', 'Relatorio de vendas do dia', 'Resumo administrativo com total vendido nos pedidos cadastrados na base de apoio da API Java.', 96.00, 5, 'admin');

INSERT INTO relatorio (tipo, titulo, descricao, valor_total, quantidade, responsavel)
VALUES ('CARDAPIO', 'Relatorio de itens ativos', 'Quantidade de itens disponiveis no cardapio da API Java.', NULL, 19, 'admin');

INSERT INTO relatorio (tipo, titulo, descricao, valor_total, quantidade, responsavel)
VALUES ('AVALIACOES', 'Relatorio de avaliacoes', 'Quantidade de avaliacoes registradas pelos clientes.', NULL, 3, 'admin');

COMMIT;