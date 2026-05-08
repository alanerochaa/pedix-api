# Arquitetura Integrada - Pedix Sprint 4

O Pedix é uma solução de comanda digital inteligente voltada para restaurantes, com foco em reduzir falhas de comunicação entre cliente, garçom, cozinha e gestão.

Na Sprint 4, a solução foi consolidada com integração entre API Java, API C#, aplicativo mobile, banco de dados e práticas de DevOps.

## Divisão de responsabilidades

### API Java - Módulo de suporte e gestão

A API Java atua como módulo secundário do ecossistema Pedix, responsável por funcionalidades administrativas e analíticas:

- Gestão de categorias do cardápio
- Gestão de itens do cardápio
- Registro de avaliações
- Histórico de pedidos
- Relatórios gerenciais
- Documentação Swagger/OpenAPI
- Health check em ambiente cloud

### API C# - Núcleo operacional

A API C# atua como módulo principal da operação do restaurante, concentrando os fluxos críticos:

- Cliente
- Garçom
- Mesa
- Comanda
- Pedido
- Pagamento

### Aplicativo Mobile

O aplicativo mobile consome as APIs do ecossistema Pedix para entregar a experiência final ao usuário.

Principais integrações previstas com a API Java:

- Consulta de cardápio
- Consulta de categorias
- Envio de avaliações
- Consulta de informações administrativas

### DevOps e Cloud

A API Java foi publicada na Microsoft Azure utilizando App Service com Java 17.

Também foi configurada uma pipeline com GitHub Actions para automatizar:

- Build da aplicação
- Geração do arquivo `.jar`
- Deploy no Azure App Service
- Atualização automática a cada push na branch `main`

## Arquitetura resumida

```txt
Mobile React Native
        |
        | HTTP/REST
        v
API Java Spring Boot  ---- API C# .NET
        |
        | JDBC
        v
Oracle Database / APE

```

Links da aplicação

API Azure:

https://pedix-api-aab0evapangybdh7.eastus-01.azurewebsites.net

Health Check:

https://pedix-api-aab0evapangybdh7.eastus-01.azurewebsites.net/api/health

Swagger:

https://pedix-api-aab0evapangybdh7.eastus-01.azurewebsites.net/swagger-ui.html

GitHub:

https://github.com/alanerochaa/pedix-api




## `pipeline-devops.md`

```md
# Pipeline DevOps - Pedix API Java

A API Java do Pedix foi integrada a uma esteira CI/CD utilizando GitHub Actions e Microsoft Azure App Service.

## Objetivo

Automatizar o processo de build e deploy da aplicação, reduzindo esforço manual e garantindo maior padronização na entrega.

## Fluxo da pipeline

```txt
Commit na branch main
        |
        v
GitHub Actions
        |
        v
Configuração do Java 17
        |
        v
Build com Maven
        |
        v
Geração do artefato .jar
        |
        v
Deploy no Azure App Service
        |
        v
API publicada em cloud

```

Etapas configuradas
1. Checkout do código

A pipeline baixa o código-fonte do repositório GitHub.

2. Configuração do Java

A pipeline configura o ambiente com Java 17, compatível com o projeto Spring Boot.

3. Build com Maven

Executa o comando:
```
mvn clean install
```

Esse processo compila a aplicação e gera o arquivo .jar.

4. Upload do artefato

O arquivo .jar é separado como artefato de build para ser utilizado na etapa de deploy.

5. Deploy no Azure

O artefato é publicado automaticamente no Azure App Service.

Recursos utilizados
GitHub
GitHub Actions
Azure App Service
Java 17
Maven
Spring Boot
Evidências

A pipeline executou com sucesso as etapas de build e deploy.

Status esperado:

build: success
deploy: success
Links

Repositório:

https://github.com/alanerochaa/pedix-api

GitHub Actions:

https://github.com/alanerochaa/pedix-api/actions

API publicada:

https://pedix-api-aab0evapangybdh7.eastus-01.azurewebsites.net

Health Check:

https://pedix-api-aab0evapangybdh7.eastus-01.azurewebsites.net/api/health


## Ajuste no `endpoints-mobile.md`

Troca a URL Azure antiga por essa:

```md
## Base URL Azure

https://pedix-api-aab0evapangybdh7.eastus-01.azurewebsites.net