# 📱 Pedix API - Comanda Digital Inteligente

## 📝 Descrição do Projeto
O **Pedix** é uma aplicação backend desenvolvida em **Java com Spring Boot** que gerencia pedidos e itens de cardápio em restaurantes, lanchonetes ou serviços de alimentação.  
A aplicação permite criar, consultar, atualizar e deletar pedidos e itens do cardápio, garantindo persistência em **banco de dados Oracle** e fornecendo uma API **RESTful** com documentação Swagger/OpenAPI.

O objetivo é proporcionar uma **gestão digital eficiente de comandas**, atendendo às solicitações do cliente de forma rápida, segura e confiável.



## 📅 Cronograma de Desenvolvimento

| Atividade                         | Responsável | Prazo      | Status      |
|-----------------------------------|-------------|------------|------------|
| Configuração do Spring Boot       | Alane       | 05/10/2025 | Concluído  |
| Criação das classes de domínio    | Alane       | 05/10/2025 | Concluído  |
| Implementação dos endpoints       | Alane       | 06/10/2025 | Concluído  |
| Testes API (Postman)              | Anna/Maria  | 09/10/2025 | Concluído  |
| Documentação e README             | Alane       | 06/10/2025 | Concluído  |


## 🧩 Visão Geral e Arquitetura

A **Pedix API** segue os princípios da **Clean Architecture** e do **Domain-Driven Design (DDD)**, garantindo baixo acoplamento e alta coesão entre as camadas da aplicação.

```mermaid
flowchart TD
    %% Camada de apresentação
    subgraph API_Camada_de_Apresentacao
        A[Controllers - RestController]
    end

    %% Camada de regras de negócio
    subgraph Application_Regras_de_Negocio
        B[Services - Service]
        G[DTOs e Validations]
    end

    %% Camada de domínio
    subgraph Domain_Modelo_de_Dominio
        C[Entidades JPA: ItemCardapio, Pedido, PedidoItem]
        H[Enums: StatusPedido, CategoriaItem]
    end

    %% Camada de infraestrutura e integrações
    subgraph Infrastructure_Persistencia_e_Integracoes
        D[Repositories - Spring Data JPA]
        E[(Banco de Dados Oracle)]
        F[API C# - Comandas e Clientes]
    end

    %% Fluxos de dependência
    A --> B
    B --> C
    B --> D
    D --> E
    C --> H
    B --> G
    B --> F
    
```

🏗️ Camadas e Responsabilidades

A arquitetura da Pedix API segue o padrão de camadas bem definidas, promovendo baixo acoplamento e alta coesão entre os componentes da aplicação.

| Camada                                   | Pacote                     | Responsabilidade                                                                                                                                                                    |
| ---------------------------------------- | -------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **Apresentação (Controller)**            | `com.pedix.api.controller` | Define os **endpoints REST** da API — exemplos: `/api/item-cardapio`, `/api/pedido`. É responsável por receber as requisições HTTP e delegar o processamento às classes de serviço. |
| **Aplicação (Service)**                  | `com.pedix.api.service`    | Implementa as **regras de negócio**, orquestra entidades e repositórios, e aplica validações funcionais antes de persistir ou retornar dados.                                       |
| **Domínio (Entities / Enums)**           | `com.pedix.api.domain`     | Contém as **entidades JPA** e **enums** que representam o modelo de negócio, como `ItemCardapio`, `Pedido`, `PedidoItem`, `StatusPedido` e `CategoriaItem`.                         |
| **Infraestrutura (Repository / Oracle)** | `com.pedix.api.repository` | Gerencia a **persistência dos dados** com **Spring Data JPA**, realizando a comunicação com o **banco Oracle** configurado na aplicação.                                            |
| **Integrações Externas (C#)**            | `REST API em .NET`         | Comunicação planejada com o **microserviço de atendimento** desenvolvido em C#, responsável por comandas, mesas e clientes.                                                         |


## ⚙️ Tecnologias Utilizadas

| Categoria       | Tecnologia           | Uso Principal                                                 |
|-----------------|--------------------|---------------------------------------------------------------|
| Linguagem       | 📦 Java 22          | Linguagem de desenvolvimento backend.                         |
| Framework       | 🌱 Spring Boot 3    | Facilita a criação de aplicações RESTful standalone.          |
| Persistência    | 🗄️ JPA / Hibernate | Mapeamento Objeto-Relacional (ORM).                           |
| Banco de Dados  | 💾 Oracle 12c       | Armazenamento persistente dos dados.                          |
| Utilitário      | ✨ Lombok           | Redução de código boilerplate (getters, setters, construtores). |
| Documentação    | 📖 Swagger / OpenAPI| Geração automática da documentação da API.                    |
| Build           | 🛠️ Maven           | Gerenciamento de dependências e ciclo de vida do projeto.     |
| Teste           | 📬 Postman / Insomnia| Teste manual dos endpoints da API.                            |

---

## 📂 Estrutura do Projeto
```
pedix.api/
├── .idea           → Configurações do ambiente de desenvolvimento (IntelliJ IDEA).
├── .mvn            → Wrappers do Maven.
├── pedix-api       → (Pasta gerada/nome do módulo)
├── src
│   ├── main
│   │   ├── java/com/pedix/api
│   │   │   ├── config      → Configurações gerais da aplicação (segurança, beans).
│   │   │   ├── controller  → Endpoints REST (API). Lida com requisições HTTP e delega para o Service.
│   │   │   ├── domain      → Entidades JPA (Modelos de domínio como Pedido, ItemCardapio, etc.).
│   │   │   ├── dto         → Data Transfer Objects (DTOs), usados com validação funcional.
│   │   │   ├── repository  → Repositórios JPA (Interfaces) para acesso ao banco de dados.
│   │   │   ├── service     → Serviços da aplicação (Business Logic) e lógica transacional.
│   │   │   ├── DatabaseInitializer → Componente para inicialização de dados (se necessário).
│   │   │   └── PedixApplication    → Classe principal que inicializa o Spring Boot.
│   │   │
│   │   └── resources
│   │       └── application.properties → Configuração do banco de dados, ambiente e Swagger.
│   │
│   └── test
│       └── java/com/pedix/api → Contém os testes unitários e de integração.
│
├── target          → Diretório gerado pelo Maven, contém os artefatos de build.
├── .gitignore      → Arquivo de ignorar arquivos para o Git.
├── pom.xml         → Arquivo de configuração do Maven.
├── README.md       → Informações e instruções iniciais do projeto.
└── HELP.md         → Arquivo de ajuda (geralmente gerado pelo Spring Initializr).
```


## 🛠 Funcionalidades
A aplicação **Pedix API** permite gerenciar **pedidos** e **itens do cardápio**, oferecendo os principais endpoints para **CRUD** (Create, Read, Update, Delete) e atualização de status de pedidos.

###  🚀  Endpoints da API Pedix

## 📦 Item do Cardápio
| Método   | Endpoint                                                        | Descrição                          |
| -------- | --------------------------------------------------------------- | ---------------------------------- |
| `GET`    | [`/api/item-cardapio`](http://localhost:8080/api/item-cardapio) | Lista todos os itens disponíveis   |
| `POST`   | `/api/item-cardapio`                                            | Cria um novo item do cardápio      |
| `PUT`    | `/api/item-cardapio/{id}`                                       | Atualiza um item existente pelo ID |
| `DELETE` | `/api/item-cardapio/{id}`                                       | Deleta um item pelo ID             |


## 🛒 Pedido

| Método | Endpoint                                                                        | Descrição                             |
| ------ | ------------------------------------------------------------------------------- | ------------------------------------- |
| `GET`  | [`/api/pedido/comanda/{comandaId}`](http://localhost:8080/api/pedido/comanda/1) | Lista todos os pedidos de uma comanda |
| `POST` | `/api/pedido`                                                                   | Cria um novo pedido                   |
| `PUT`  | `/api/pedido/{id}/status?status=PRONTO`                                         | Atualiza o status de um pedido        |


## 💻 Exemplos de Request/Response para testar

### 🍽️ Endpoints de Item do Cardápio

📝 GET /api/item-cardapio - Lista todos os itens disponíveis.
🔗 URL de teste: http://localhost:8080/api/item-cardapio

✅ Resposta esperada:
```
[
  {
    "id": 1,
    "comandaId": 1001,
    "item": {
      "id": 1,
      "nome": "Pizza Calabresa",
      "descricao": "Deliciosa pizza com calabresa",
      "preco": 35.00,
      "categoria": "PRATO",
      "disponivel": true,
      "imagemUrl": null
    },
    "quantidade": 1,
    "status": "EM_PREPARO",
    "observacao": "Sem queijo ralado",
    "dataHora": "2025-10-05T21:00:00"
  },
  {
    "id": 2,
    "comandaId": 1002,
    "item": {
      "id": 2,
      "nome": "Refrigerante",
      "descricao": "Coca Cola 350ml",
      "preco": 8.50,
      "categoria": "BEBIDA",
      "disponivel": true,
      "imagemUrl": null
    },
    "quantidade": 2,
    "status": "PRONTO",
    "observacao": "Um com gelo, outro sem",
    "dataHora": "2025-10-05T20:30:00"
  },
  {
    "id": 3,
    "comandaId": 1003,
    "item": {
      "id": 3,
      "nome": "Sorvete Chocolate",
      "descricao": "Sobremesa gelada",
      "preco": 12.00,
      "categoria": "SOBREMESA",
      "disponivel": true,
      "imagemUrl": null
    },
    "quantidade": 1,
    "status": "ENTREGUE",
    "observacao": "Entrega prioridade",
    "dataHora": "2025-10-05T19:00:00"
  },
  {
    "id": 4,
    "comandaId": 1004,
    "item": {
      "id": 1,
      "nome": "Pizza Calabresa",
      "descricao": "Deliciosa pizza com calabresa",
      "preco": 35.00,
      "categoria": "PRATO",
      "disponivel": true,
      "imagemUrl": null
    },
    "quantidade": 1,
    "status": "CANCELADO",
    "observacao": "Cliente desistiu do pedido",
    "dataHora": "2025-10-05T20:50:00"
  }
]
```

➕ POST /api/item-cardapio — Cria um novo item do cardápio
🔗 URL de teste: http://localhost:8080/api/item-cardapio

📤 Exemplo de Requisição:

```

{
"nome": "Hambúrguer",
"descricao": "Hambúrguer com queijo e bacon",
"preco": 25.50,
"categoria": "PRATO",
"disponivel": true,
"imagemUrl": null
}
```

✅ Resposta esperada:

```
{
  "mensagem": "🍔 Item do cardápio criado com sucesso!"
}

```

✏️ PUT /api/item-cardapio/{id} — Atualiza um item existente (ex: id = 1)

🔗 URL de teste: http://localhost:8080/api/item-cardapio/1

📤 Exemplo de Requisição:

```
{
  "nome": "Pizza Calabresa Grande",
  "descricao": "Pizza com calabresa e extra queijo",
  "preco": 40.00,
  "categoria": "PRATO",
  "disponivel": true,
  "imagemUrl": null
}

```
✅ Resposta esperada:

```
{
  "mensagem": "✅ Item do cardápio atualizado com sucesso!"
}


```

🗑️ DELETE /api/item-cardapio/{id} — Deleta um item do cardápio (ex: id = 2)

🔗 URL de teste: http://localhost:8080/api/item-cardapio/2

✅ Resposta esperada:
```
{
  "mensagem": "🗑️ Item do cardápio removido com sucesso!"
}

```

### 🧾 Endpoints de Pedido

📄 GET /api/pedido/comanda/{comandaId} — Lista pedidos de uma comanda (ex: comandaId = 1001)

🔗 URL de teste: http://localhost:8080/api/pedido/comanda/1001

✅ Resposta esperada:

```

[
{
"id": 1,
"comandaId": 1001,
"item": {
"id": 1,
"nome": "Pizza Calabresa",
"descricao": "Deliciosa pizza com calabresa",
"preco": 35.00,
"categoria": "PRATO",
"disponivel": true,
"imagemUrl": null
},
"quantidade": 1,
"status": "EM_PREPARO",
"observacao": "Sem queijo ralado",
"dataHora": "2025-10-05T21:00:00"
},
{
"id": 2,
"comandaId": 1001,
"item": {
"id": 2,
"nome": "Refrigerante",
"descricao": "Coca Cola 350ml",
"preco": 8.50,
"categoria": "BEBIDA",
"disponivel": true,
"imagemUrl": null
},
"quantidade": 2,
"status": "EM_PREPARO",
"observacao": "Um com gelo, outro sem",
"dataHora": "2025-10-05T21:05:00"
}
]
```

➕ POST /api/pedido — Cria um novo pedido

🔗 URL de teste: http://localhost:8080/api/pedido

📤 Exemplo de Requisição:
```
{
  "comandaId": 1005,
  "itemId": 2,
  "quantidade": 1,
  "observacao": "Com gelo e limão"
}

```

✅ Resposta esperada:
```
{
  "mensagem": "🧾 Pedido criado com sucesso!",
}

```

🔄 PUT /api/pedido/{id}/status?status=PRONTO — Atualiza o status de um pedido (ex: id = 1)

🔗 URL de teste: http://localhost:8080/api/pedido/1/status?status=PRONTO

✅ Resposta esperada:

```
{
"mensagem": "✅ Status do pedido atualizado com sucesso!"
}
```


💡 Status possíveis:

| Código          | Descrição                                 |
| --------------- |-------------------------------------------|
| 🕐 `EM_PREPARO` | Pedido em preparo na cozinha              |
| ✅ `PRONTO`      | Pedido finalizado e pronto                |
| 🚚 `ENTREGUE`   | Pedido entregue ao cliente                |
| ❌ `CANCELADO`   | Pedido cancelado pelo cliente  |



## 🛡 Validação Funcional
Todos os DTOs utilizam **anotações de validação** do Jakarta Bean Validation:
- `@NotNull` – campos obrigatórios
- `@NotBlank` – strings não podem ser vazias
- `@Positive` – valores numéricos devem ser positivos
- `@Size` – limites de tamanho de strings

---


🗃️ Script SQL (Oracle)

Criação de Tabelas e Dados Iniciais


-- 🔹 Este script cria as tabelas principais da aplicação Pedix:
-- ITEM_CARDAPIO → Representa os itens disponíveis no cardápio.
-- PEDIDO        → Representa os pedidos realizados pelos clientes.
-- PEDIDO_ITEM   → Relaciona os pedidos com os itens do cardápio (N:N).



-- 1️⃣ Tabela ITEM_CARDAPIO
-- Cada registro representa um prato, bebida ou sobremesa do cardápio.
-- Possui informações de nome, preço, categoria e disponibilidade.

```
CREATE TABLE item_cardapio (
id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
nome VARCHAR2(255) NOT NULL,
descricao VARCHAR2(500),
preco NUMBER(10,2) NOT NULL,
categoria VARCHAR2(50),
disponivel NUMBER(1) DEFAULT 1,
imagem_url VARCHAR2(500)
);
```

2️⃣ Tabela PEDIDO
-- Armazena os pedidos realizados por uma comanda.
-- Cada pedido pode conter vários itens, e seu total é calculado pela soma dos subtotais em PEDIDO_ITEM.
```
CREATE TABLE pedido (
id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
id_comanda NUMBER NOT NULL,
status VARCHAR2(50) DEFAULT 'EM_PREPARO',
observacao VARCHAR2(500),
data_hora TIMESTAMP DEFAULT SYSTIMESTAMP,
total NUMBER(12,2) DEFAULT 0
);
```

3️⃣ Tabela PEDIDO_ITEM
-- Tabela de relacionamento N:N entre PEDIDO e ITEM_CARDAPIO.
-- Cada registro indica um item dentro de um pedido, com quantidade e preço no momento da venda.
```
CREATE TABLE pedido_item (
id NUMBER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
pedido_id NUMBER NOT NULL,
item_cardapio_id NUMBER NOT NULL,
quantidade NUMBER(5) NOT NULL,
preco_unitario NUMBER(10,2) NOT NULL,
subtotal NUMBER(12,2) NOT NULL,
CONSTRAINT fk_pedido FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
CONSTRAINT fk_item FOREIGN KEY (item_cardapio_id) REFERENCES item_cardapio(id)
);
```
🍕 Inserts iniciais - ITEM_CARDAPIO

```
INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Pizza Calabresa', 'Deliciosa pizza com calabresa', 35.00, 'PRATO', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Refrigerante', 'Coca Cola 350ml', 8.50, 'BEBIDA', 1, NULL);

INSERT INTO item_cardapio (nome, descricao, preco, categoria, disponivel, imagem_url)
VALUES ('Sorvete Chocolate', 'Sobremesa gelada', 12.00, 'SOBREMESA', 1, NULL);
```
-- ====================================================
-- 🧾 Inserts iniciais - PEDIDO e PEDIDO_ITEM
-- ====================================================
-- Estes exemplos simulam pedidos com diferentes status:
-- EM_PREPARO, PRONTO, ENTREGUE e CANCELADO.
```
-- 🕐 Pedido 1: EM_PREPARO
INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1001, 'EM_PREPARO', 'Sem queijo ralado', 35.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (1, 1, 1, 35.00, 35.00);
```
-- ✅ Pedido 2: PRONTO
INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1002, 'PRONTO', 'Um com gelo, outro sem', 17.00);
```
INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (2, 2, 2, 8.50, 17.00);
```
-- 🚚 Pedido 3: ENTREGUE
INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1003, 'ENTREGUE', 'Entrega prioridade', 12.00);
```
INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (3, 3, 1, 12.00, 12.00);
```
-- ❌ Pedido 4: CANCELADO
```
INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1004, 'CANCELADO', 'Cliente desistiu do pedido', 0.00);
```

```

## 🚀 Como Rodar a Aplicação

1. Clonar o repositório:
```
git clone https://github.com/alanerochaa/pedix-api.git
cd pedix-api
```

2. Configurar variáveis de ambiente (opcional):
```
   DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
   DB_USER=RM561052
   DB_PASSWORD= Disponível documentação.
   API_KEY= Disponível documentação.
```

3. Build e execução com Maven:
```
   mvn clean install
   mvn spring-boot:run
```

4. Acessar a documentação Swagger:
```bash
http://localhost:8080/swagger-ui.html
```


📊 Diagramas

Diagrama de Entidade e Relacionamento (DER) → Relacionamento entre Pedido e ItemCardapio
![img_1.png](img_1.png)

Diagrama de Classes → Classes Pedido, ItemCardapio, DTOs e enumerações
![img.png](img.png)

(Inclua imagens no diretório docs/ e link no README)


📋 Testes da API

* Todas as rotas foram testadas no Postman.


> Desenvolvido com 💜 por **CodeGirls**

### 👩‍💻 Integrantes:
- [**Alane Rocha da Silva rm561052**](https://github.com/alanerochaa)


- [**Anna Beatriz Bonfim rm559561**](https://github.com/annabonfim)


- [**Maria Eduarda Araujo rm560944**](https://github.com/DudaAraujo14)  
 
