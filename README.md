# 📱 Pedix API - Comanda Digital Inteligente

## 📝 Descrição do Projeto
O **Pedix** é uma aplicação backend desenvolvida em **Java com Spring Boot** que gerencia pedidos e itens de cardápio em restaurantes, lanchonetes ou serviços de alimentação.  
A aplicação permite criar, consultar, atualizar e deletar pedidos e itens do cardápio, garantindo persistência em **banco de dados Oracle** e fornecendo uma API **RESTful** com documentação Swagger/OpenAPI.

O objetivo é proporcionar uma **gestão digital eficiente de comandas**, atendendo às solicitações do cliente de forma rápida, segura e confiável.

## 🚀 Evolução — Sprint 2

Na Sprint 2, a Pedix API foi aprimorada para atingir o Nível 3 de maturidade REST, através da implementação do Spring HATEOAS.
Agora, cada recurso retorna links autoexplicativos (_links), permitindo descoberta dinâmica de rotas diretamente nas respostas da API.

Principais avanços:

* Implementação do HATEOAS nas entidades ItemCardapio e Pedido;

* Criação do endpoint /home com links navegáveis para os recursos principais;

* Documentação detalhada via Swagger UI;

* Padronização de DTOs e validações com Bean Validation;

* Tratamento global de exceções com respostas amigáveis;

* Código totalmente refatorado para aderência ao REST Nível 3.

> 🧠 Com isso, a API evoluiu de um CRUD REST básico (Nível 2) para uma API navegável e autodescritiva (Nível 3).


## 🗓️ Cronograma Macro do Projeto

| Sprint       | Período                  | Objetivos Principais                                | Entregáveis / Funcionalidades                                                                                                                                                                                                                                                                                                 | Status                 |
| ------------ | ------------------------ | --------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ---------------------- |
| **Sprint 1** | 25/09/2025 → 12/10/2025  | Estrutura inicial do ecossistema Pedix              | - Criação das **APIs base (Java e C#)**<br>- Configuração de **persistência Oracle**<br>- CRUD de `ItemCardapio` e `Pedido`<br>- Documentação Swagger inicial                                                                                                                                                                 | ✅ Concluído            |
| **Sprint 2** | 13/10/2025 → 06/11/2025  | Elevação de maturidade técnica e integração parcial | - Implementação de **HATEOAS (REST Nível 3)**<br>- Refatoração e DTOs padronizados<br>- Validações e Exception Handler global<br>- **Integração entre API Java e API .NET (base de dados compartilhada)**<br>- Testes versionados (Postman v1/v2)<br>- Telas iniciais do **App Mobile (React Native)** integradas com as APIs | ✅ Concluído            |
| **Sprint 3** | Prevista p/ 1º Sem. 2026 | Expansão funcional e automação de processos         | - Novas classes para **entidades auxiliares**: `Mesa`, `Cliente`, `Garcom`, `Comanda`<br>- Integração completa entre **Java ↔ .NET ↔ Mobile**<br>- Implementação de **notificações em tempo real (WebSocket ou SignalR)**<br>- Módulo de **Dashboard Administrativo** com métricas                                            | 🧩 Em planejamento     |
| **Sprint 4** | Prevista p/ 2º Sem. 2026 | Fase de estabilidade, segurança e deploy            | - **DevOps (CI/CD com GitHub Actions e Azure)**<br>- Logs e monitoramento de métricas<br>- Autenticação (JWT) e controle de acesso por perfil<br>- **Deploy de ambiente produtivo** com pipeline automatizado                                                                                                                 | 🕓 Backlog estratégico |


## 📱 Interface do Aplicativo Pedix

<p align="center">
  <img src="docs/imagens/tela-inicial-app.png" alt="Tela inicial do app Pedix" width="250">
  <br>
  <em>Tela inicial do aplicativo Pedix</em>
</p>


| Data       | Atividade                                                       | Responsável          | Status       |
| ---------- | --------------------------------------------------------------- | -------------------- |--------------|
| 20/09/2025 | Configuração inicial do projeto (Spring Boot + Oracle)          | **Alane Rocha**      | ✅ Concluído  |
| 21/09/2025 | Implementação do CRUD de `ItemCardapio`                         | **Alane Rocha**      | ✅ Concluído  |
| 23/09/2025 | Criação do `PedidoController` e integração com Oracle           | **Alane Rocha**      | ✅ Concluído  |
| 25/09/2025 | Testes iniciais e documentação Swagger                          | **Alane Rocha**      | ✅ Concluído  |
| 28/10/2025 | Implementação do HATEOAS e refatoração dos controllers          | **Alane Rocha**      | ✅ Concluído  |
| 28/10/2025 | Atualização do README, coleção Postman e geração das evidências | **Alane Rocha**      | ✅ Concluído  |
| 31/10/2025 | Geração dos diagramas e gravação do vídeo de apresentação       | **Alane Rocha**      | ✅ Concluído  |
| 05/11/2025 | Testes finais e validação completa do sistema                   | **Equipe CodeGirls** | ✅ Concluído  |




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
## 🔗 Implementação do HATEOAS

A API utiliza o módulo Spring HATEOAS para adicionar links de navegação aos recursos retornados.
Cada entidade (ItemCardapio, Pedido) é empacotada em um EntityModel<> contendo links para operações relacionadas.

Exemplo de implementação: 

```
EntityModel<ItemCardapio> model = EntityModel.of(item,
    linkTo(methodOn(ItemCardapioController.class).buscarPorId(item.getId())).withSelfRel(),
    linkTo(methodOn(ItemCardapioController.class).listar(null)).withRel("todos_itens")
);
```

* Com isso, o cliente pode navegar entre os recursos sem conhecer previamente as URIs.



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
pedix-api/
├── .idea/                          → Configurações do IntelliJ IDEA
├── .mvn/                           → Arquivos de suporte do Maven Wrapper
│
├── docs/                           → Documentação do projeto
│   ├── cronograma
│   │   ├── Cronograma-java-advanced.pdf
│   ├── diagramas/
│   │   ├── diagrama-classes-pedix.png   → Diagrama de Classes (UML da aplicação Java)
│   │   └── diagrama-mer-pedix.png       → Modelo Entidade-Relacionamento (MER) do banco Oracle
│   │
│   ├── imagens/
│   │   ├── tela-inicial-app.png         → Tela inicial do aplicativo mobile Pedix
│   │   └── colecao-postman/             → Evidências visuais dos testes dos endpoints (prints do Postman)
│   │       └── 1-GET-home-hateoas.png   → (Novo) Retorno HATEOAS do endpoint `/home`
│   │       ├── 2-GET-item-cardapio.png
│   │       ├── 3-GET-item-cardapio-ID.png
│   │       ├── 4-POST-item-cardápio.png
│   │       ├── 5-PUT-item-cardápio-ID.png
│   │       ├── 6-DELETE-item-cardápio-ID.png
│   │       ├── 7-GET-listar-pedidos.png
│   │       ├── 8-GET-listar-pedidos-ID.png
│   │       ├── 9-GET-listar-pedido-comandaID.png
│   │       ├── 10-POST-cria-pedido-vinculado-comanda.png
│   │       ├── 11-PUT-atualiza-status-pedido.png
│   │       ├── 12-DELETE-pedido-ID.png
│   │       ├── 13-GET-teste-erro-404.png
│   │       ├── 14-POST-teste-erro-400.png
│   │       
│   └── testes/
│       └── pedix_api_postman._v1.json      
        └── pedix_api_postman._v2.json 
│
├── src/
│   ├── main/
│   │   ├── java/com/pedix/api/
│   │   │   ├── controller/              → Camada de controle (endpoints REST)
│   │   │   │   ├── HomeController.java         → Novo endpoint `/home` com HATEOAS
│   │   │   │   ├── ItemCardapioController.java → CRUD dos itens do cardápio com HATEOAS
│   │   │   │   └── PedidoController.java       → CRUD de pedidos com HATEOAS
│   │   │   │
│   │   │   ├── domain/                  → Entidades JPA (modelo de domínio)
│   │   │   │   ├── enums/               → Enumerações usadas nas entidades
│   │   │   │   │   ├── CategoriaItem.java
│   │   │   │   │   └── StatusPedido.java
│   │   │   │   ├── ItemCardapio.java
│   │   │   │   ├── Pedido.java
│   │   │   │   └── PedidoItem.java
│   │   │   │
│   │   │   ├── dto/                     → DTOs e objetos de transporte de dados
│   │   │   │   ├── ItemCardapioDTO.java
│   │   │   │   ├── MensagemResponse.java       → (Novo) DTO genérico para respostas padronizadas
│   │   │   │   ├── PedidoDTO.java
│   │   │   │   ├── PedidoItemDTO.java
│   │   │   │   └── PedidoResponseDTO.java
│   │   │   │
│   │   │   ├── exception/               → Tratamento global de exceções (400, 404, etc.)
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │
│   │   │   ├── repository/              → Repositórios JPA
│   │   │   │   ├── ItemCardapioRepository.java
│   │   │   │   └── PedidoRepository.java
│   │   │   │
│   │   │   ├── service/                 → Regras de negócio (camada de serviço)
│   │   │   │   ├── ItemCardapioService.java
│   │   │   │   └── PedidoService.java
│   │   │   │
│   │   │   └── PedixApplication.java    → Classe principal (entry point do Spring Boot)
│   │   │
│   │   └── resources/
│   │       ├── application.properties   → Configurações do banco Oracle e Swagger
│   │       └── data.sql                 → Script SQL inicial com inserts automáticos para testes
│   │
│   └── test/
│       └── java/com/pedix/api/
│           └── ApplicationTests.java    → Testes unitários e de integração (JUnit)
│
├── target/                              → Diretório gerado pelo Maven após build
├── pom.xml                              → Configurações e dependências Maven
├── README.md                            → Documentação principal do projeto (Sprint 1 + Sprint 2)
├── .gitignore                           → Arquivo de exclusão do Git
└── .gitattributes                       → Definições de atributos de versionamento
```


## 🛠 Funcionalidades

A aplicação **Pedix API** oferece as seguintes funcionalidades principais:

- 🍽️ **Gerenciamento de Itens do Cardápio** — criação, listagem, atualização e exclusão de pratos, bebidas e sobremesas.
- 🧾 **Gestão de Pedidos** — criação, consulta, atualização e exclusão de pedidos vinculados a comandas.
- 🔄 **Atualização de Status** — permite alterar o status dos pedidos em tempo real (`EM_PREPARO`, `PRONTO`, `ENTREGUE`, `CANCELADO`).
- 🧱 **Persistência em Banco Oracle** — todos os dados são armazenados de forma segura e consistente no banco de dados relacional.
- 📖 **API RESTful Documentada** — acesso via Swagger UI para explorar e testar os endpoints interativamente.
- 💡 Agora, todas as respostas dos endpoints incluem hipermídia HATEOAS e mensagens padronizadas.


## 🌐 URLs principais da API

Antes de testar os endpoints, é possível verificar o status da API e acessar sua documentação interativa:


| Finalidade                     | URL                                                                                          | Descrição                                                                                                                                                |
| ------------------------------ | -------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **🏠 Endpoint inicial (Home)** | [`http://localhost:8080/home`](http://localhost:8080/home)                                   | Exibe mensagem de status confirmando que a API está rodando.<br>🟢 *Exemplo:* `🚀 API Pedix está rodando! Acesse o Swagger UI para testar os endpoints.` |
| **📖 Documentação Swagger UI** | [`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html) | Interface visual gerada automaticamente pelo SpringDoc OpenAPI, permitindo testar todos os endpoints REST de forma interativa.                           |


###  🚀  Endpoints da API Pedix

## 🧭 Endpoint HATEOAS – /home

📍 URL: http://localhost:8080/home

🔍 Exemplo de Resposta JSON

```
{
  "mensagem": "API Pedix está rodando! Acesse o Swagger UI ou as rotas principais.",
  "_links": {
    "self": { "href": "http://localhost:8080/home" },
    "pedidos": { "href": "http://localhost:8080/api/pedido" },
    "cardapio": {
      "href": "http://localhost:8080/api/item-cardapio{?categoria}",
      "templated": true
    },
    "swagger-ui": { "href": "/swagger-ui/index.html" }
  }
}

```
> O endpoint /home serve como ponto de entrada da API, retornando mensagem de status e links navegáveis para os principais recursos.

## 📦 Cardápio — ItemCardápio
| Método   | Endpoint                             | Descrição                                                    | Exemplo de uso                                                                                                     |
| :------- | :----------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------------------------------------------------------------- |
| `GET`    | `/api/item-cardapio`                 | Lista todos os itens disponíveis no cardápio.                | [http://localhost:8080/api/item-cardapio](http://localhost:8080/api/item-cardapio)                                 |
| `GET`    | `/api/item-cardapio?categoria=PRATO` | Filtra itens por categoria (`PRATO`, `BEBIDA`, `SOBREMESA`). | [http://localhost:8080/api/item-cardapio?categoria=PRATO](http://localhost:8080/api/item-cardapio?categoria=PRATO) |
| `GET`    | `/api/item-cardapio/{id}`            | Busca um item específico pelo ID.                            | [http://localhost:8080/api/item-cardapio/1](http://localhost:8080/api/item-cardapio/1)                             |
| `POST`   | `/api/item-cardapio`                 | Cria um novo item do cardápio.                               | —                                                                                                                  |
| `PUT`    | `/api/item-cardapio/{id}`            | Atualiza os dados de um item existente.                      | —                                                                                                                  |
| `DELETE` | `/api/item-cardapio/{id}`            | Exclui um item do cardápio.                                  | —                                                                                                                  |


## 🛒 Pedido
| Método   | Endpoint                                | Descrição                                        | Exemplo de uso                                                                                 |
| :------- | :-------------------------------------- | :----------------------------------------------- |:-----------------------------------------------------------------------------------------------|
| `GET`    | `/api/pedido`                           | Lista **todos os pedidos** cadastrados.          | [http://localhost:8080/api/pedido](http://localhost:8080/api/pedido)                           |
| `GET`    | `/api/pedido/{id}`                      | Busca um pedido específico pelo ID.              | [http://localhost:8080/api/pedido/1](http://localhost:8080/api/pedido/1)                       |
| `GET`    | `/api/pedido/comanda/{comandaId}`       | Lista todos os pedidos vinculados a uma comanda. | [http://localhost:8080/api/pedido/comanda/1001](http://localhost:8080/api/pedido/comanda/1001) |
| `POST`   | `/api/pedido/comanda/{comandaId}`       | Cria um novo pedido vinculado a uma comanda.     | —                                                                                              |
| `PUT`    | `/api/pedido/{id}/status?status=PRONTO` | Atualiza o status de um pedido existente.        | —                                                                                              |
| `DELETE` | `/api/pedido/{id}`                      | Remove um pedido existente pelo ID.              | [http://localhost:8080/api/pedido/3](http://localhost:8080/api/pedido/3)                       |


🧾 Item de Pedido — PedidoItem

| Método   | Endpoint                | Descrição                                | Exemplo                                                                            |
| -------- | ----------------------- | ---------------------------------------- | ---------------------------------------------------------------------------------- |
| `GET`    | `/api/pedido-item`      | Lista todos os itens de pedido.          | [http://localhost:8080/api/pedido-item](http://localhost:8080/api/pedido-item)     |
| `GET`    | `/api/pedido-item/{id}` | Busca um item de pedido pelo ID.         | [http://localhost:8080/api/pedido-item/1](http://localhost:8080/api/pedido-item/1) |
| `POST`   | `/api/pedido-item`      | Cria um novo item vinculado a um pedido. | —                                                                                  |
| `PUT`    | `/api/pedido-item/{id}` | Atualiza um item de pedido existente.    | —                                                                                  |
| `DELETE` | `/api/pedido-item/{id}` | Remove um item de pedido.                | —                                                                                  |




## 💻 Exemplos de Request/Response para testar

### 🍽️ Endpoints de Item do Cardápio

📝 GET /api/item-cardapio - Lista todos os itens disponíveis.

* 🔗 URL de teste: GET http://localhost:8080/api/item-cardapio

✅ Resposta esperada:
```
[
  {
    "id": 1,
    "nome": "Pizza Calabresa",
    "descricao": "Deliciosa pizza com calabresa",
    "categoria": "PRATO",
    "preco": 35,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/1"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  },
  {
    "id": 2,
    "nome": "Refrigerante",
    "descricao": "Coca Cola 350ml",
    "categoria": "BEBIDA",
    "preco": 8.5,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/2"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  },
  {
    "id": 3,
    "nome": "Sorvete Chocolate",
    "descricao": "Sobremesa gelada",
    "categoria": "SOBREMESA",
    "preco": 12,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/3"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  },
  {
    "id": 4,
    "nome": "Pizza Mussarela",
    "descricao": "Pizza de mussarela com borda recheada",
    "categoria": "PRATO",
    "preco": 38,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/4"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  },
  {
    "id": 5,
    "nome": "Pizza Frango",
    "descricao": "Pizza de frango com catupiry",
    "categoria": "PRATO",
    "preco": 40,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/5"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  },
  {
    "id": 6,
    "nome": "Suco Laranja",
    "descricao": "Suco natural 300ml",
    "categoria": "BEBIDA",
    "preco": 7.5,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/6"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  },
  {
    "id": 7,
    "nome": "Salada Caesar",
    "descricao": "Salada com alface, frango e molho caesar",
    "categoria": "PRATO",
    "preco": 25,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/7"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  },
  {
    "id": 8,
    "nome": "Brownie",
    "descricao": "Brownie de chocolate com nozes",
    "categoria": "SOBREMESA",
    "preco": 10,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/8"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  },
  {
    "id": 9,
    "nome": "Água Mineral",
    "descricao": "Água sem gás 500ml",
    "categoria": "BEBIDA",
    "preco": 5,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/9"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  },
  {
    "id": 10,
    "nome": "Pizza Portuguesa",
    "descricao": "Pizza com presunto, ovos e azeitonas",
    "categoria": "PRATO",
    "preco": 42,
    "disponivel": true,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/item-cardapio/10"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/item-cardapio{?categoria}"
      }
    ]
  }
]
```

🔍 GET /api/pedido/{id} — Buscar pedido por ID

* 🔗 URL de teste: http://localhost:8080/api/pedido/1

✅ Resposta esperada:
```
{
  "id": 1,
  "nome": "Pizza Calabresa",
  "descricao": "Deliciosa pizza com calabresa",
  "categoria": "PRATO",
  "preco": 35,
  "disponivel": true,
  "imagemUrl": null,
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/item-cardapio/1"
    },
    "todos_itens": {
      "href": "http://localhost:8080/api/item-cardapio{?categoria}",
      "templated": true
    }
  }
```

➕ POST /api/item-cardapio — Cria um novo item do cardápio
* 🔗 URL de teste: http://localhost:8080/api/item-cardapio

📤 Exemplo de Requisição:

{
"nome": "Lasanha Bolonhesa",
"descricao": "Lasanha tradicional com molho bolonhesa e queijo gratinado",
"preco": 42.50,
"categoria": "PRATO",
"disponivel": true
}


✅ Resposta esperada:

```
{
  "item": {
    "id": 11,
    "nome": "Lasanha Bolonhesa",
    "descricao": "Lasanha tradicional com molho bolonhesa e queijo gratinado",
    "categoria": "PRATO",
    "preco": 42.5,
    "disponivel": true
  },
  "mensagem": "Item do cardápio criado com sucesso!",
  "_links": {
    "todos_itens": "http://localhost:8080/api/item-cardapio",
    "self": "http://localhost:8080/api/item-cardapio/11"
  }
}

```

✏️ PUT /api/item-cardapio/{id} — Atualiza um item existente (ex: id = 1)

* 🔗 URL de teste: http://localhost:8080/api/item-cardapio/1


📤 Exemplo de Requisição:
```
{
"nome": "Lasanha quatro queijos",
"descricao": "Lasanha tradicional com molho bolonhesa e queijo gratinado",
"preco": 42.50,
"categoria": "PRATO",
"disponivel": true
}


```
✅ Resposta esperada:

```
{
  "mensagem": "Item do cardápio atualizado com sucesso!",
  "_links": {
    "todos_itens": "http://localhost:8080/api/item-cardapio",
    "self": "http://localhost:8080/api/item-cardapio/1"
  },
  "item": {
    "id": 1,
    "nome": "Lasanha quatro queijos",
    "descricao": "Lasanha tradicional com molho bolonhesa e queijo gratinado",
    "categoria": "PRATO",
    "preco": 42.5,
    "disponivel": true,
    "imagemUrl": null
  }
}
```

🗑️ DELETE /api/item-cardapio/{id} — Deleta um item do cardápio (ex: id = 10)

* 🔗 URL de teste: http://localhost:8080/api/item-cardapio/10

✅ Resposta esperada:
```
{
  "mensagem": " Item do cardápio removido com sucesso!",
  "timestamp": "2025-11-06T20:39:09.2439547",
  "status": 200
}
```

### 🧾 Endpoints de Pedido

📦 GET /api/pedido — Lista todos os pedidos cadastrados

* 🔗 URL de teste: http://localhost:8080/api/pedido
```
[
  {
    "id": 1,
    "comandaId": 1001,
    "status": "EM_PREPARO",
    "dataCriacao": "2025-10-19T10:45:24.94496",
    "observacao": "Sem queijo ralado",
    "total": 35,
    "itens": [
      {
        "itemCardapioId": 1,
        "nome": "Pizza Calabresa",
        "quantidade": 1,
        "precoUnitario": 35,
        "subtotal": 35
      }
    ],
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/pedido/1"
      },
      {
        "rel": "todos_pedidos",
        "href": "http://localhost:8080/api/pedido"
      }
    ]
  },
  {
    "id": 2,
    "comandaId": 1002,
    "status": "PRONTO",
    "dataCriacao": "2025-10-19T10:45:25.015956",
    "observacao": "Um com gelo, outro sem",
    "total": 17,
    "itens": [
      {
        "itemCardapioId": 2,
        "nome": "Refrigerante",
        "quantidade": 2,
        "precoUnitario": 8.5,
        "subtotal": 17
      }
    ],
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/pedido/2"
      },
      {
        "rel": "todos_pedidos",
        "href": "http://localhost:8080/api/pedido"
      }
    ]
  },
  {
    "id": 3,
    "comandaId": 1003,
    "status": "EM_PREPARO",
    "dataCriacao": "2025-10-19T10:45:25.028863",
    "observacao": "Sem cebola",
    "total": 45,
    "itens": [
      {
        "itemCardapioId": 5,
        "nome": "Pizza Frango",
        "quantidade": 1,
        "precoUnitario": 40,
        "subtotal": 40
      }
    ],
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/pedido/3"
      },
      {
        "rel": "todos_pedidos",
        "href": "http://localhost:8080/api/pedido"
      }
    ]
  }
]
```

📄 GET /api/pedido/comanda/{comandaId} — Lista pedidos de uma comanda (ex: comandaId = 1001)

* 🔗 URL de teste: http://localhost:8080/api/pedido/comanda/1001

✅ Resposta esperada:

```
[
  {
    "id": 1,
    "comandaId": 1001,
    "status": "EM_PREPARO",
    "dataCriacao": "2025-10-19T10:45:24.94496",
    "observacao": "Sem queijo ralado",
    "total": 35,
    "itens": [
      {
        "itemCardapioId": 1,
        "nome": "Pizza Calabresa",
        "quantidade": 1,
        "precoUnitario": 35,
        "subtotal": 35
      }
    ]
  }
]
```

➕ POST /api/pedido/comanda/{comandaId} — Cria um novo pedido vinculado a uma comanda

* 🔗 URL de teste: http://localhost:8080/api/pedido/comanda/1002

📤 Exemplo de Requisição:

```
{
"itens": [
{ "itemCardapioId": 2, "quantidade": 2 },
{ "itemCardapioId": 3, "quantidade": 1 }
],
"observacao": "Um refrigerante sem gelo"
}
```
✅ Resposta esperada:

```
{
  "_links": {
    "self": "http://localhost:8080/api/pedido/4",
    "todos_pedidos": "http://localhost:8080/api/pedido"
  },
  "mensagem": "Pedido criado com sucesso!",
  "pedido": {
    "id": 4,
    "comandaId": 1001,
    "status": "EM_PREPARO",
    "dataCriacao": null,
    "observacao": "Um refrigerante sem gelo",
    "total": 29,
    "itens": [
      {
        "itemCardapioId": 2,
        "nome": "Refrigerante",
        "quantidade": 2,
        "precoUnitario": 8.5,
        "subtotal": 17
      },
      {
        "itemCardapioId": 3,
        "nome": "Sorvete Chocolate",
        "quantidade": 1,
        "precoUnitario": 12,
        "subtotal": 12
      }
    ]
  }
}
```


🔄 PUT /api/pedido/{id}/status?status=PRONTO — Atualiza o status de um pedido (ex: id = 1)

* 🔗 URL de teste: http://localhost:8080/api/pedido/1/status?status=PRONTO

✅ Resposta esperada:

```
{
  "_links": {
    "todos_pedidos": "http://localhost:8080/api/pedido",
    "self": "http://localhost:8080/api/pedido/1"
  },
  "mensagem": "Status do pedido atualizado com sucesso!",
  "pedido": {
    "id": 1,
    "comandaId": 1001,
    "status": "PRONTO",
    "dataCriacao": "2025-10-19T10:45:24.94496",
    "observacao": "Sem queijo ralado",
    "total": 35,
    "itens": [
      {
        "itemCardapioId": 1,
        "nome": "Pizza Calabresa",
        "quantidade": 1,
        "precoUnitario": 35,
        "subtotal": 35
      }
    ]
  }
}

```

🗑️ DELETE /api/pedido/{id} — Deletar pedido por ID
* 🔗 URL de teste: http://localhost:8080/api/pedido/6

📤 Requisição:
Não é necessário enviar corpo — apenas o ID do pedido na URL.

```
{
  "status": 200,
  "mensagem": " Pedido removido com sucesso!",
  "timestamp": "2025-11-06T20:46:17.2942339"
}
```



### 🧾 Endpoints de Item de Pedido

📦 GET /api/pedido-item - Listar todos os itens do pedido
* 🔗 URL de teste: http://localhost:8080/api/pedido-item

```
[
  {
    "id": 2,
    "pedidoId": 2,
    "itemCardapioId": 2,
    "nomeItem": "Refrigerante",
    "quantidade": 2,
    "precoUnitario": 8.5,
    "subtotal": 17,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/pedido-item/2"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/pedido-item"
      },
      {
        "rel": "pedido",
        "href": "http://localhost:8080/api/pedido/2"
      }
    ]
  },
  {
    "id": 3,
    "pedidoId": 3,
    "itemCardapioId": 5,
    "nomeItem": "Pizza Frango",
    "quantidade": 1,
    "precoUnitario": 40,
    "subtotal": 40,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/pedido-item/3"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/pedido-item"
      },
      {
        "rel": "pedido",
        "href": "http://localhost:8080/api/pedido/3"
      }
    ]
  },
  {
    "id": 4,
    "pedidoId": 4,
    "itemCardapioId": 2,
    "nomeItem": "Refrigerante",
    "quantidade": 2,
    "precoUnitario": 8.5,
    "subtotal": 17,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/pedido-item/4"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/pedido-item"
      },
      {
        "rel": "pedido",
        "href": "http://localhost:8080/api/pedido/4"
      }
    ]
  },
  {
    "id": 5,
    "pedidoId": 4,
    "itemCardapioId": 3,
    "nomeItem": "Sorvete Chocolate",
    "quantidade": 1,
    "precoUnitario": 12,
    "subtotal": 12,
    "links": [
      {
        "rel": "self",
        "href": "http://localhost:8080/api/pedido-item/5"
      },
      {
        "rel": "todos_itens",
        "href": "http://localhost:8080/api/pedido-item"
      },
      {
        "rel": "pedido",
        "href": "http://localhost:8080/api/pedido/4"
      }
    ]
  }
]
```


📄 GET /api/pedido-item{id} — Busca item de pedido por ID (ex = 2)

* 🔗 URL de teste: http://localhost:8080/api/pedido-item/2

```
{
"id": 2,
"pedidoId": 2,
"itemCardapioId": 2,
"nomeItem": "Refrigerante",
"quantidade": 2,
"precoUnitario": 8.5,
"subtotal": 17,
"_links": {
"self": {
"href": "http://localhost:8080/api/pedido-item/2"
},
"todos_itens": {
"href": "http://localhost:8080/api/pedido-item"
},
"pedido": {
"href": "http://localhost:8080/api/pedido/2"
}
}
}
```

➕ POST /api/pedido-item — Cria um novo pedido 
* 🔗 URL de teste: http://localhost:8080/api/pedido-item

📤 Exemplo de Requisição:
```
{
"pedidoId": 4,
"itemCardapioId": 2,
"quantidade": 2,
"precoUnitario": 8.5
}
```

✅ Resposta esperada:
```
{
"item": {
"id": 6,
"pedidoId": 4,
"itemCardapioId": 2,
"nomeItem": "Refrigerante",
"quantidade": 2,
"precoUnitario": 8.5,
"subtotal": 17
},
"mensagem": "Item de pedido criado com sucesso!",
"_links": {
"self": "http://localhost:8080/api/pedido-item/6",
"pedido": "http://localhost:8080/api/pedido/4",
"todos_itens": "http://localhost:8080/api/pedido-item"
}
}
```

🔄 PUT /api/pedido-item{ID} — Atualiza o status de um pedido (ex: id = 2)

* 🔗 URL de teste:

📤 Exemplo de Requisição:


```
{
    
    "pedidoId": 2,
    "itemCardapioId": 2,
    "nomeItem": "Refrigerante com gelo ",
    "quantidade": 2,
    "precoUnitario": 8.5
}
   
```

✅ Resposta esperada:

```
{
  "item": {
    "id": 2,
    "pedidoId": 2,
    "itemCardapioId": 2,
    "nomeItem": "Refrigerante",
    "quantidade": 2,
    "precoUnitario": 8.5,
    "subtotal": 17
  },
  "mensagem": "Item de pedido atualizado com sucesso!",
  "_links": {
    "self": "http://localhost:8080/api/pedido-item/2",
    "pedido": "http://localhost:8080/api/pedido/2",
    "todos_itens": "http://localhost:8080/api/pedido-item"
  }
}
```

🗑️ DELETE /api/pedido-item/{id} — Deletar pedido por ID
* 🔗 URL de teste: http://localhost:8080/api/pedido-item/

✅ Resposta esperada:

```
{
  "status": 200,
  "mensagem": "Item de pedido removido com sucesso!",
  "_links": {
    "pedido": "http://localhost:8080/api/pedido/4",
    "todos_itens": "http://localhost:8080/api/pedido-item"
  },
  "timestamp": "2025-11-06T21:25:07.1866765"
}
```

💡 Status possíveis de Pedido

| Código             | Descrição                     |
| :----------------- | :---------------------------- |
| 🧑‍🍳 `EM_PREPARO` | Pedido em preparo na cozinha  |
| ✅ `PRONTO`         | Pedido finalizado e pronto    |
| 🚚 `ENTREGUE`      | Pedido entregue ao cliente    |
| ❌ `CANCELADO`      | Pedido cancelado pelo cliente |



## 🛡 Validação Funcional
Todos os DTOs utilizam **anotações de validação** do Jakarta Bean Validation:
- `@NotNull` – campos obrigatórios
- `@NotBlank` – strings não podem ser vazias
- `@Positive` – valores numéricos devem ser positivos
- `@Size` – limites de tamanho de strings

---

## 🗃️ Script SQL (Oracle)
Criação de Tabelas, Sequences, Triggers e Dados Iniciais — Sistema Pedix

### 🧹 Reset do Banco (Seguro)
```
BEGIN
FOR t IN (SELECT table_name FROM user_tables) LOOP
EXECUTE IMMEDIATE 'DROP TABLE "' || t.table_name || '" CASCADE CONSTRAINTS';
END LOOP;
END;
/
```

## 🔢 Sequences

```
BEGIN
EXECUTE IMMEDIATE 'DROP SEQUENCE item_cardapio_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP SEQUENCE pedido_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
EXECUTE IMMEDIATE 'DROP SEQUENCE pedido_item_seq';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

CREATE SEQUENCE item_cardapio_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE pedido_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
CREATE SEQUENCE pedido_item_seq START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
```

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


## 🧩 Criação das Tabelas

### 1️⃣ Tabela ITEM_CARDAPIO

Representa os itens disponíveis no cardápio (pratos, bebidas e sobremesas).

```
CREATE TABLE item_cardapio (
id              NUMBER PRIMARY KEY,
nome            VARCHAR2(255) NOT NULL,
descricao       VARCHAR2(500),
preco           NUMBER(10,2) NOT NULL,
categoria       VARCHAR2(50),
disponivel      NUMBER(1) DEFAULT 1,
imagem_url      VARCHAR2(500)
);
```

### 2️⃣ Tabela PEDIDO

Armazena os pedidos vinculados a uma comanda.

```
CREATE TABLE pedido (
id              NUMBER PRIMARY KEY,
id_comanda      NUMBER NOT NULL,
status          VARCHAR2(50) DEFAULT 'EM_PREPARO',
observacao      VARCHAR2(500),
data_hora       TIMESTAMP DEFAULT SYSTIMESTAMP,
total           NUMBER(12,2) DEFAULT 0
);
```

### 3️⃣ Tabela PEDIDO_ITEM

Relação N:N entre pedidos e itens do cardápio.
```
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
```

## ⚙️ Triggers (Auto Incremento via Sequence)
```
CREATE OR REPLACE TRIGGER trg_item_cardapio_id
BEFORE INSERT ON item_cardapio
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
SELECT item_cardapio_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

----------------------------------------------------------
CREATE OR REPLACE TRIGGER trg_pedido_id
BEFORE INSERT ON pedido
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
  SELECT pedido_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/
------------------------------------------------------------
CREATE OR REPLACE TRIGGER trg_pedido_item_id
BEFORE INSERT ON pedido_item
FOR EACH ROW
WHEN (NEW.id IS NULL)
BEGIN
  SELECT pedido_item_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/
```

## 🍕 Inserts Iniciais — ITEM_CARDAPIO
```
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
```

## 🧾 Inserts Iniciais — PEDIDO e PEDIDO_ITEM

### 🕐 Pedido 1 — EM_PREPARO

```
INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1001, 'EM_PREPARO', 'Sem queijo ralado', 35.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (1, 1, 1, 35.00, 35.00);
```

### ✅ Pedido 2 — PRONTO
```
INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1002, 'PRONTO', 'Um com gelo, outro sem', 17.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (2, 2, 2, 8.50, 17.00);

```

### 🚚 Pedido 3 — EM_PREPARO

```
INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1003, 'EM_PREPARO', 'Sem cebola', 45.00);

INSERT INTO pedido_item (pedido_id, item_cardapio_id, quantidade, preco_unitario, subtotal)
VALUES (3, 5, 1, 40.00, 40.00);

```

### ❌ Pedido 4 — CANCELADO
```
INSERT INTO pedido (id_comanda, status, observacao, total)
VALUES (1004, 'CANCELADO', 'Cliente desistiu do pedido', 0.00);
```

### 💾 Commit Final
```
COMMIT;
```

## 📋 Resumo Estrutural
| Tabela          | Descrição                                        | Chave Primária | Relações                                                               |
| :-------------- | :----------------------------------------------- | :------------- | :--------------------------------------------------------------------- |
| `ITEM_CARDAPIO` | Itens do cardápio (pratos, bebidas, sobremesas). | `id`           | —                                                                      |
| `PEDIDO`        | Pedido de uma comanda, com data e total.         | `id`           | 1:N → `PEDIDO_ITEM`                                                    |
| `PEDIDO_ITEM`   | Itens que pertencem a um pedido específico.      | `id`           | FK `pedido_id` → `PEDIDO` <br> FK `item_cardapio_id` → `ITEM_CARDAPIO` |




## 🚀 Como Rodar a Aplicação

1. Clonar o repositório:
```
git clone https://github.com/alanerochaa/pedix-api.git
cd pedix-api
```

2. Configurar variáveis de ambiente (opcional):
```
   DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL
   DB_USER=RMXXXXXX
   DB_PASSWORD=XXXXXX
```

3. Build e execução com Maven:
```
   mvn clean install
   mvn spring-boot:run
```

4. Acessar a documentação Swagger:
```bash
http://localhost:8080/swagger-ui/index.html
```


## 📊 Diagramas

### Diagrama Entidade-Relacionamento (DER)
O diagrama abaixo representa o modelo conceitual completo da aplicação Pedix, integrando todos os módulos do sistema e refletindo a estrutura compartilhada do banco de dados Oracle.

```
          ┌────────────────────────────┐
          │        Banco Oracle        │
          │  (Modelo de Dados Único)   │
          └────────────┬───────────────┘
                       │
     ┌─────────────────┴───────────────────┐
     │                                     │
┌───────────────┐                 ┌─────────────────┐
│  API Principal│                 │  API Pedix Java │
│   (C#/.NET)   │                 │ (Spring Boot)   │
│---------------│                 │-----------------│
│ Cliente       │                 │ ItemCardapio    │
│ Garçom        │                 │ Pedido          │
│ Mesa          │                 │ PedidoItem      │
│ Comanda       │                 │                 │
└───────────────┘                 └─────────────────┘
```

## ⚙️ Arquitetura Geral

### 🖥️ API Principal (C#/.NET) → Responsável pela gestão operacional do restaurante, incluindo Clientes, Garçons, Mesas e Comandas.

### ☕ API Complementar (Java/Spring Boot) → Responsável pela gestão do Cardápio e dos Pedidos, integrando-se à API principal através do banco Oracle.

## 💡 Observação:

A API Java implementa e manipula as entidades: ITEM_CARDAPIO, PEDIDO e PEDIDO_ITEM.

As demais entidades (CLIENTE, GARCOM, MESA, COMANDA) pertencem à API principal em C#.

O diagrama abaixo representa o modelo conceitual completo do banco de dados **Oracle** utilizado pela aplicação **Pedix**.  
Ele demonstra as entidades, atributos e relacionamentos que sustentam o funcionamento das APIs Java e C#

![DER completo](docs/diagramas/diagrama-mer-pedix.png)


### 🧱 Diagrama de Classes (UML)
Mostra as classes principais da aplicação Java, seus atributos e relacionamentos, além dos *enums* utilizados (`CategoriaItem`, `StatusPedido`).

![Diagrama de Classes Pedix](docs/diagramas/diagrama-classes-pedix.png)

---

## 🧪 Testes no Postman

A coleção completa de testes da API está disponível para importação no **Postman**.  
O arquivo inclui todos os endpoints (`GET`, `POST`, `PUT`, `DELETE`) com exemplos de requisição e resposta, além de cenários de erro e exceções tratadas globalmente (`404`, `400`).

## Coleções Postman por Sprint

📌 **Sprint 1 – Testes dos Endpoints Básicos (CRUD + REST Nível 1)**  
📄 **[Baixar coleção Sprint 1](docs/testes/pedix_api_postman._v1.json)**

📌 **Sprint 2 – Testes da Evolução (HATEOAS + maturidade nível 3 + refatorações)**  
📄 **[Baixar coleção Sprint 2](docs/testes/pedix_api_postman._v2.json)**


---

### 📸 Coleção de Testes — Postman

Abaixo estão capturas de tela de todos os testes executados com sucesso:

<div align="center">

| Método | Descrição | Imagem                                                                                 |
|:-------|:-----------|:---------------------------------------------------------------------------------------|
| 🟢 **GET** | **Endpoint inicial — `/home`** | ![GET Home](docs/imagens/colecao-postman/1-GET-home.png)                               |
| 🟢 **GET** | **Listar todos os itens do cardápio** | ![GET Itens do Cardápio](docs/imagens/colecao-postman/2-GET-item-cardapio.png)         |
| 🟢 **GET** | **Buscar item do cardápio por ID** | ![GET por ID](docs/imagens/colecao-postman/3-GET-item-cardapio-ID.png)                 |
| 🟡 **POST** | **Criar novo item no cardápio** | ![POST Cardápio](docs/imagens/colecao-postman/4-POST-item-cardapio.png)                |
| 🟠 **PUT** | **Atualizar item existente no cardápio** | ![PUT Atualizar Item](docs/imagens/colecao-postman/5-PUT-item-cardapio-ID.png)         |
| 🔴 **DELETE** | **Remover item do cardápio** | ![DELETE Item](docs/imagens/colecao-postman/6-DELETE-item-cardapio-ID.png)             |
| 🟢 **GET** | **Listar todos os pedidos** | ![GET Pedidos](docs/imagens/colecao-postman/7-GET-listar-pedidos.png)                  |
| 🟢 **GET** | **Buscar pedido por ID** | ![GET Pedido ID](docs/imagens/colecao-postman/8-GET-Listar-pedidos-ID.png)             |
| 🟢 **GET** | **Listar pedidos pelo número da comanda** | ![GET Comanda](docs/imagens/colecao-postman/9-GET-listar-pedido-comandaID.png)         |
| 🟡 **POST** | **Criar novo pedido vinculado a uma comanda** | ![POST Pedido](docs/imagens/colecao-postman/10-POST-cria-pedido-vinculado-comanda.png) |
| 🟠 **PUT** | **Atualizar status do pedido** | ![PUT Pedido](docs/imagens/colecao-postman/11-PUT-Atualiza-status-pedido.png)          |
| 🔴 **DELETE** | **Deletar pedido por ID** | ![DELETE Pedido](docs/imagens/colecao-postman/12-DELETE-pedido-ID.png)                 |
| ⚠️ **GET** | **Teste de erro `EntityNotFoundException (404)`** | ![GET 404](docs/imagens/colecao-postman/13-GET-teste-erro-404.png)                     |
| ⚠️ **POST** | **Teste de erro `IllegalArgumentException (400)`** | ![POST 400](docs/imagens/colecao-postman/14-POST-teste-erro-400.png)                   |

</div>

✅ **Todos os endpoints foram testados com sucesso**, com respostas esperadas e tratamento global de exceções ativo.

---

## 🎬 Vídeo de Apresentação

O vídeo de apresentação demonstra o funcionamento completo da aplicação Pedix API, incluindo a execução dos endpoints, testes no Postman e integração com o banco de dados Oracle.

📺 **Assista aqui:** [Apresentação Pedix API - CodeGirls](https://www.youtube.com/watch?v=IoaK88IQYF4)
🧾 O vídeo mostra a execução dos endpoints no Postman, o retorno HATEOAS do /home e encerra com o caso de uso integrado entre API, aplicativo e banco Oracle.

--- 

## 👩‍💻 Integrantes e Responsabilidades

| Nome Completo | RM | Função no Projeto | GitHub |
|----------------|----|------------------|--------|
| **Alane Rocha da Silva** | RM561052 | Desenvolvimento da API Java (Spring Boot), integração com banco Oracle e documentação | [@alanerochaa](https://github.com/alanerochaa) |
| **Anna Beatriz Bonfim** | RM559561 | Desenvolvimento do aplicativo mobile (React Native) e integração com IoT | [@annabonfim](https://github.com/annabonfim) |
| **Maria Eduarda Araujo Penas** | RM560944 | Desenvolvimento da API C# e DevOps  | [@DudaAraujo14](https://github.com/DudaAraujo14) |


<p align="center">
  Desenvolvido com 💜 pela equipe <strong>CodeGirls</strong> — FIAP 2025.
</p>