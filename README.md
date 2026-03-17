🪪 Microserviço de Cartões
📄 Descrição
Microserviço responsável pela criação, ativação e cancelamento de cartões físicos e online, com persistência em banco de dados relacional e publicação de eventos em mensageria (RabbitMQ).
Este projeto foi desenvolvido como prova técnica, com foco em clareza, organização, boas práticas e regras de negócio.

🛠️ Tecnologias Utilizadas
Java 17+
Spring Boot
Spring Web
Spring Data JPA
Banco de dados relacional (PostgreSQL / H2)
RabbitMQ
Spring AMQP
Lombok
Springdoc OpenAPI (Swagger)
Docker e Docker Compose

🏗️ Arquitetura
O projeto segue uma arquitetura em camadas:
Controller: exposição dos endpoints REST
Service: regras de negócio
Repository: acesso a dados
Domain: entidades, enums e eventos
Config: configurações de infraestrutura (RabbitMQ, etc.)

📌 Funcionalidades
✅ Criação de Cartões
Cria automaticamente dois cartões:
Cartão Físico (situação inicial: PENDENTE_ATIVACAO)
Cartão Online (situação inicial: ATIVO)
Persiste os cartões no banco de dados
Publica evento de criação para cada cartão
✅ Ativação de Cartão
Permite ativar apenas cartões físicos
Só é permitido se o cartão estiver em PENDENTE_ATIVACAO
✅ Cancelamento de Cartão
Permite cancelar cartões ativos
Após o cancelamento, o cartão passa para a situação CANCELADO

🌐 Endpoints
➕ Criar cartões
POST /cartoes
jsonCopiar código
{
  "cpf": "12345678901",
  "nomeImpresso": "JOAO DA SILVA",
  "produto": "CREDITO",
  "subproduto": "PLATINUM"
}



▶️ Ativar cartão
POST /cartoes/{id}/ativar
Regras:
Apenas cartão FÍSICO
Situação deve ser PENDENTE_ATIVACAO

⛔ Cancelar cartão
POST /cartoes/{id}/cancelar
Regras:
Situação deve ser ATIVO

📖 Documentação da API
A documentação da API está disponível via Swagger:
Copiar código
http://localhost:8080/swagger-ui.html



🐳 Executando o projeto com Docker
Pré-requisitos
Docker
Docker Compose
Subir a aplicação
bashCopiar código
docker-compose up


Serviços disponíveis:
Aplicação: http://localhost:8080
RabbitMQ Management: http://localhost:15672
PostgreSQL: localhost:5432

▶️ Executando localmente (sem Docker)
Pré-requisitos
Java 17+
Maven
RabbitMQ em execução
Banco de dados configurado
bashCopiar código
mvn spring-boot:run



⚙️ Configurações
As configurações de banco e mensageria estão definidas em:
application.yml
application-local.yml
application-docker.yml

✅ Observações
Não foi implementado controle de autenticação/autorização
Testes automatizados não são obrigatórios, mas podem ser adicionados como melhoria
O foco do projeto é clareza, organização e boas práticas

👤 Autor
Desenvolvido por Eliesio Junior Targino de Lima.
