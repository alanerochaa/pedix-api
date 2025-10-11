-- ====================================================
-- RESET DO BANCO - Remove tabelas existentes (seguro)
-- ====================================================
BEGIN
  FOR t IN (SELECT table_name FROM user_tables) LOOP
    EXECUTE IMMEDIATE 'DROP TABLE "' || t.table_name || '" CASCADE CONSTRAINTS';
  END LOOP;
END;
/
-- ====================================================
-- SEQUENCES
-- ====================================================

BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE item_cardapio_seq';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE pedido_seq';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

BEGIN
  EXECUTE IMMEDIATE 'DROP SEQUENCE pedido_item_seq';
EXCEPTION
  WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE item_cardapio_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE pedido_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE pedido_item_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- ====================================================
-- TABELAS
-- ====================================================

CREATE TABLE item_cardapio (
  id              NUMBER PRIMARY KEY,
  nome            VARCHAR2(255) NOT NULL,
  descricao       VARCHAR2(500),
  preco           NUMBER(10,2) NOT NULL,
  categoria       VARCHAR2(50),
  disponivel      NUMBER(1) DEFAULT 1,
  imagem_url      VARCHAR2(500)
);

CREATE TABLE pedido (
  id              NUMBER PRIMARY KEY,
  id_comanda      NUMBER NOT NULL,
  status          VARCHAR2(50) DEFAULT 'EM_PREPARO',
  observacao      VARCHAR2(500),
  data_hora       TIMESTAMP DEFAULT SYSTIMESTAMP,
  total           NUMBER(12,2) DEFAULT 0
);

CREATE TABLE pedido_item (
  id                NUMBER PRIMARY KEY,
  pedido_id         NUMBER NOT NULL,
  item_cardapio_id  NUMBER NOT NULL,
  quantidade        NUMBER(5) NOT NULL,
  preco_unitario    NUMBER(10,2) NOT NULL,
  subtotal          NUMBER(12,2) NOT NULL,
  CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
  CONSTRAINT fk_item FOREIGN KEY (item_cardapio_id) REFERENCES item_cardapio(id)
);

-- ===================================================
-- TRIGGERS (auto incremento via sequence)
-- ====================================================

CREATE OR REPLACE TRIGGER trg_item_cardapio_id
BEFORE INSERT ON item_cardapio
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
  SELECT item_cardapio_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_pedido_id
BEFORE INSERT ON pedido
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
  SELECT pedido_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_pedido_item_id
BEFORE INSERT ON pedido_item
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
  SELECT pedido_item_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

-- ====================================================
-- DADOS DE EXEMPLO
-- ====================================================

-- Itens do cardápio
INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Pizza Calabresa', 'Deliciosa pizza com calabresa', 35.00, 'PRATO', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Refrigerante', 'Coca Cola 350ml', 8.50, 'BEBIDA', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Sorvete Chocolate', 'Sobremesa gelada', 12.00, 'SOBREMESA', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Pizza Mussarela', 'Pizza de mussarela com borda recheada', 38.00, 'PRATO', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Pizza Frango', 'Pizza de frango com catupiry', 40.00, 'PRATO', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Suco Laranja', 'Suco natural 300ml', 7.50, 'BEBIDA', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Salada Caesar', 'Salada com alface, frango e molho caesar', 25.00, 'PRATO', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Brownie', 'Brownie de chocolate com nozes', 10.00, 'SOBREMESA', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Água Mineral', 'Água sem gás 500ml', 5.00, 'BEBIDA', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Pizza Portuguesa', 'Pizza com presunto, ovos e azeitonas', 42.00, 'PRATO', 1, NULL);

-- Pedidos
INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1001, 'EM_PREPARO', 'Sem queijo ralado', 35.00);

INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1002, 'PRONTO', 'Um com gelo, outro sem', 17.00);

INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1003, 'EM_PREPARO', 'Sem cebola', 45.00);

-- Itens dos pedidos
INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (1, 1, 1, 35.00, 35.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (2, 2, 2, 8.50, 17.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (3, 5, 1, 40.00, 40.00);

COMMIT;