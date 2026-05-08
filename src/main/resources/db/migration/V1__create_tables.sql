-- ====================================================
-- V1__create_tables.sql
-- Criacao da estrutura inicial do Pedix
-- API Java - Secundaria/Suporte e Gestao
-- ====================================================

CREATE SEQUENCE categoria_cardapio_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE item_cardapio_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE pedido_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE pedido_item_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE avaliacao_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE historico_pedido_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE relatorio_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE categoria_cardapio (
    id          NUMBER PRIMARY KEY,
    nome        VARCHAR2(100) NOT NULL,
    descricao   VARCHAR2(300),
    ativo       NUMBER(1) DEFAULT 1 NOT NULL,
    CONSTRAINT uk_categoria_cardapio_nome UNIQUE (nome),
    CONSTRAINT ck_categoria_cardapio_ativo CHECK (ativo IN (0, 1))
);

CREATE TABLE item_cardapio (
    id             NUMBER PRIMARY KEY,
    categoria_id   NUMBER NOT NULL,
    nome           VARCHAR2(120) NOT NULL,
    descricao      VARCHAR2(500),
    preco          NUMBER(12,2) NOT NULL,
    disponivel     NUMBER(1) DEFAULT 1 NOT NULL,
    imagem_url     VARCHAR2(500),
    CONSTRAINT fk_item_cardapio_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria_cardapio(id),
    CONSTRAINT ck_item_cardapio_disponivel CHECK (disponivel IN (0, 1)),
    CONSTRAINT ck_item_cardapio_preco CHECK (preco >= 0)
);

CREATE TABLE pedido (
    id                  NUMBER PRIMARY KEY,
    id_comanda          NUMBER NOT NULL,
    status              VARCHAR2(50) DEFAULT 'EM_PREPARO' NOT NULL,
    observacao          VARCHAR2(500),
    total               NUMBER(12,2) DEFAULT 0 NOT NULL,
    data_hora           TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    garcom_responsavel  VARCHAR2(150),
    CONSTRAINT ck_pedido_status CHECK (
        status IN ('EM_PREPARO', 'PRONTO', 'ENTREGUE', 'FINALIZADO', 'CANCELADO')
    ),
    CONSTRAINT ck_pedido_total CHECK (total >= 0)
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
    CONSTRAINT ck_pedido_item_quantidade CHECK (quantidade > 0),
    CONSTRAINT ck_pedido_item_preco CHECK (preco_unitario >= 0),
    CONSTRAINT ck_pedido_item_subtotal CHECK (subtotal >= 0)
);

CREATE TABLE avaliacao (
    id               NUMBER PRIMARY KEY,
    pedido_id        NUMBER,
    item_cardapio_id NUMBER,
    nome_cliente     VARCHAR2(150),
    nota             NUMBER(1) NOT NULL,
    comentario       VARCHAR2(500),
    data_avaliacao   TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT fk_avaliacao_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido(id),
    CONSTRAINT fk_avaliacao_item_cardapio
        FOREIGN KEY (item_cardapio_id)
        REFERENCES item_cardapio(id),
    CONSTRAINT ck_avaliacao_nota CHECK (nota BETWEEN 1 AND 5)
);

CREATE TABLE historico_pedido (
    id              NUMBER PRIMARY KEY,
    pedido_id       NUMBER NOT NULL,
    status_anterior VARCHAR2(50),
    status_novo     VARCHAR2(50) NOT NULL,
    descricao       VARCHAR2(500),
    data_registro   TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    usuario         VARCHAR2(150),
    CONSTRAINT fk_historico_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido(id)
        ON DELETE CASCADE
);

CREATE TABLE relatorio (
    id              NUMBER PRIMARY KEY,
    tipo            VARCHAR2(80) NOT NULL,
    titulo          VARCHAR2(150) NOT NULL,
    descricao       VARCHAR2(500),
    valor_total     NUMBER(12,2),
    quantidade      NUMBER(10),
    data_geracao    TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    responsavel     VARCHAR2(150),
    CONSTRAINT ck_relatorio_valor CHECK (valor_total IS NULL OR valor_total >= 0),
    CONSTRAINT ck_relatorio_quantidade CHECK (quantidade IS NULL OR quantidade >= 0)
);

CREATE OR REPLACE TRIGGER trg_categoria_cardapio_id
BEFORE INSERT ON categoria_cardapio
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
    SELECT categoria_cardapio_seq.NEXTVAL
      INTO :NEW.id
      FROM dual;
END;
/

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

CREATE OR REPLACE TRIGGER trg_avaliacao_id
BEFORE INSERT ON avaliacao
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
    SELECT avaliacao_seq.NEXTVAL
      INTO :NEW.id
      FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_historico_pedido_id
BEFORE INSERT ON historico_pedido
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
    SELECT historico_pedido_seq.NEXTVAL
      INTO :NEW.id
      FROM dual;
END;
/

CREATE OR REPLACE TRIGGER trg_relatorio_id
BEFORE INSERT ON relatorio
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
    SELECT relatorio_seq.NEXTVAL
      INTO :NEW.id
      FROM dual;
END;
/