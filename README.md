# 🪪 Microserviço de Cartões

Microserviço responsável pela **criação, ativação e cancelamento de cartões físicos e online**, com persistência em banco de dados relacional e publicação de eventos em mensageria (**RabbitMQ**).

Este projeto foi desenvolvido como **prova técnica**, com foco em **clareza, organização, boas práticas e regras de negócio**.

---

## 📄 Descrição

O serviço gerencia o ciclo de vida de cartões, permitindo:

- criação de cartões físicos e online;
- ativação de cartões físicos;
- cancelamento de cartões ativos;
- persistência em banco de dados relacional;
- publicação de eventos de criação via mensageria.

---

## 🛠️ Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL / H2
- RabbitMQ
- Spring AMQP
- Lombok
- Springdoc OpenAPI (Swagger)
- Docker
- Docker Compose

---

## 🏗️ Arquitetura

O projeto segue uma arquitetura em camadas:

- **Controller**: exposição dos endpoints REST
- **Service**: regras de negócio
- **Repository**: acesso a dados
- **Domain**: entidades, enums e eventos
- **Config**: configurações de infraestrutura, como RabbitMQ

---

## 📌 Funcionalidades

### ✅ Criação de cartões

Ao criar um cartão, o sistema gera automaticamente:

- **Cartão físico**
  - situação inicial: `PENDENTE_ATIVACAO`
- **Cartão online**
  - situação inicial: `ATIVO`

Além disso, o sistema:

- persiste os cartões no banco de dados;
- publica um evento de criação para cada cartão.

### ✅ Ativação de cartão

Permite ativar **apenas cartões físicos**.

**Regras:**
- o cartão deve ser do tipo **FÍSICO**;
- a situação atual deve ser `PENDENTE_ATIVACAO`.

### ✅ Cancelamento de cartão

Permite cancelar cartões ativos.

**Regra:**
- a situação atual deve ser `ATIVO`.

Após o cancelamento, o cartão passa para a situação `CANCELADO`.

---

## 🌐 Endpoints

### ➕ Criar cartões

**POST** `/cartoes`

#### Exemplo de requisição

```json
{
  "cpf": "12345678901",
  "nomeImpresso": "JOAO DA SILVA",
  "produto": "CREDITO",
  "subproduto": "PLATINUM"
}

## 🐇 RabbitMQ e Banco

O projeto sobe com **H2 em memória** por padrão.

Se quiser usar infraestrutura local com containers, existe um [docker-compose.yml](docker-compose.yml) com:

- RabbitMQ em `localhost:5672`
- RabbitMQ Management em `http://localhost:15672`
- PostgreSQL em `localhost:5432`

### Subir apenas RabbitMQ

```powershell
docker compose up -d rabbitmq
```

### Subir RabbitMQ e PostgreSQL

```powershell
docker compose up -d rabbitmq postgres
```

### Rodar com H2

```powershell
.\mvnw.cmd spring-boot:run
```

### Rodar com PostgreSQL

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=postgres"
```

O profile `postgres` usa as propriedades definidas em [src/main/resources/application-postgres.yml](src/main/resources/application-postgres.yml).

---

## 👤 Autor

Desenvolvido por Eliesio Junior Targino de Lima.
