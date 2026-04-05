-- ====================================================
-- V1__create_tables.sql
-- Criacao da estrutura inicial do Pedix
-- ====================================================

CREATE SEQUENCE item_cardapio_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE pedido_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE pedido_item_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE item_cardapio (
    id           NUMBER PRIMARY KEY,
    nome         VARCHAR2(120) NOT NULL,
    descricao    VARCHAR2(500),
    categoria    VARCHAR2(30) NOT NULL,
    preco        NUMBER(12,2) NOT NULL,
    disponivel   NUMBER(1) DEFAULT 1 NOT NULL,
    imagem_url   VARCHAR2(500),
    CONSTRAINT ck_item_cardapio_disponivel CHECK (disponivel IN (0, 1))
);

CREATE TABLE pedido (
    id                  NUMBER PRIMARY KEY,
    id_comanda          NUMBER NOT NULL,
    status              VARCHAR2(50) DEFAULT 'EM_PREPARO' NOT NULL,
    observacao          VARCHAR2(500),
    total               NUMBER(12,2) DEFAULT 0 NOT NULL,
    data_hora           TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    garcom_responsavel  VARCHAR2(150)
);

CREATE TABLE pedido_item (
    id                NUMBER PRIMARY KEY,
    pedido_id         NUMBER NOT NULL,
    item_cardapio_id  NUMBER NOT NULL,
    quantidade        NUMBER(5) NOT NULL,
    preco_unitario    NUMBER(10,2) NOT NULL,
    subtotal          NUMBER(12,2) NOT NULL,
    CONSTRAINT fk_pedido_item_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido(id)
        ON DELETE CASCADE,
    CONSTRAINT fk_pedido_item_cardapio
        FOREIGN KEY (item_cardapio_id)
        REFERENCES item_cardapio(id),
    CONSTRAINT ck_pedido_item_quantidade CHECK (quantidade > 0)
);

CREATE OR REPLACE TRIGGER trg_item_cardapio_id
BEFORE INSERT ON item_cardapio
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
    SELECT item_cardapio_seq.NEXTVAL
      INTO :NEW.id
      FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_pedido_id
BEFORE INSERT ON pedido
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
    SELECT pedido_seq.NEXTVAL
      INTO :NEW.id
      FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_pedido_item_id
BEFORE INSERT ON pedido_item
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
    SELECT pedido_item_seq.NEXTVAL
      INTO :NEW.id
      FROM dual;
END;
/