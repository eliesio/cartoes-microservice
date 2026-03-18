# Microservico de Cartoes

Microservico Spring Boot para criacao, ativacao e cancelamento de cartoes fisicos e online, com persistencia em PostgreSQL e publicacao de eventos no RabbitMQ.

## Descricao do projeto

O servico implementa o ciclo de vida de cartoes com foco em regras de negocio claras:

- criacao automatica de um cartao `FISICO` e um `ONLINE`
- ativacao apenas de cartao fisico em `PENDENTE_ATIVACAO`
- cancelamento apenas de cartao em `ATIVO`
- persistencia relacional com Spring Data JPA
- publicacao de eventos de criacao, ativacao e cancelamento no RabbitMQ

## Tecnologias usadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Spring AMQP
- Lombok
- Springdoc OpenAPI
- Docker e Docker Compose
- JUnit 5 e Mockito

## Estrutura do projeto

```text
br.com.exemplo.cartoes
├── config
├── controller
│   └── dto
├── domain
│   ├── entity
│   ├── enums
│   └── event
├── exception
├── repository
└── service
```

## Profiles

- `local`: conecta em PostgreSQL e RabbitMQ expostos em `localhost`
- `docker`: conecta nos servicos `postgres` e `rabbitmq` da rede do Docker Compose

O projeto usa `local` como profile padrao.

## Como subir com Docker

Para subir aplicacao, PostgreSQL e RabbitMQ:

```bash
docker compose up --build
```

Para executar em background:

```bash
docker compose up -d --build
```

## Como rodar localmente

1. Suba PostgreSQL e RabbitMQ:

```bash
docker compose up -d postgres rabbitmq
```

2. Rode a aplicacao com o profile local.

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

macOS:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## Passos de teste da aplicacao

### Windows PowerShell / macOS

Subir o ambiente:

```bash
docker compose up -d --build
```

Verificar os containers:

```bash
docker compose ps
```

Consultar os cartoes gravados no PostgreSQL:

```bash
docker exec -it cartoes-postgres psql -U cartoes -d cartoesdb -c "select id, cpf, tipo_cartao, situacao, data_criacao from cartoes order by id;"
```

Parar o ambiente:

```bash
docker compose down
```

Parar o ambiente removendo os volumes:

```bash
docker compose down -v
```

## URLs

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`
- RabbitMQ Management: `http://localhost:15672`
- PostgreSQL: `localhost:5432`

Credenciais padrao do RabbitMQ:

- usuario: `guest`
- senha: `guest`

## Endpoints

- `POST /cartoes`
- `POST /cartoes/{id}/ativar`
- `POST /cartoes/{id}/cancelar`

## Exemplos de chamadas HTTP

### Criar cartoes

```http
POST /cartoes
Content-Type: application/json

{
  "cpf": "12345678901",
  "nomeImpresso": "JOAO DA SILVA",
  "produto": "CREDITO",
  "subproduto": "PLATINUM"
}
```

Observe os `id`s retornados na resposta. Use o `id` do cartao `FISICO` para ativacao e o `id` de um cartao em situacao `ATIVO` para cancelamento.

### Ativar cartao

```http
POST /cartoes/{idDoCartaoFisico}/ativar
```

PowerShell:

```powershell
Invoke-WebRequest -Method POST http://localhost:8080/cartoes/{idDoCartaoFisico}/ativar
```

### Cancelar cartao

```http
POST /cartoes/{idDoCartaoAtivo}/cancelar
```

PowerShell:

```powershell
Invoke-WebRequest -Method POST http://localhost:8080/cartoes/{idDoCartaoAtivo}/cancelar
```

## Regras de negocio

- Ao criar, o sistema gera:
  - um cartao `FISICO` com situacao `PENDENTE_ATIVACAO`
  - um cartao `ONLINE` com situacao `ATIVO`
- So cartoes `FISICO` em `PENDENTE_ATIVACAO` podem ser ativados
- So cartoes em `ATIVO` podem ser cancelados
- Erros de regra retornam `400`
- Cartoes inexistentes retornam `404`

## Testes

Para executar os testes.

Windows PowerShell:

```powershell
.\mvnw.cmd test
```

macOS:

```bash
./mvnw test
```

## Autor

Desenvolvido por Eliesio Junior Targino de Lima.
