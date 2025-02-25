# Sistema de Gestão de Clientes com Kafka

### 📌 Visão Geral
Este projeto consiste em um sistema distribuído de gestão de clientes com comunicação assíncrona via Apache Kafka. O sistema é composto por três microsserviços principais:

1. **ms-customer-v1** - Responsável pelo CRUD de clientes e pela publicação de eventos no Kafka.
2. **ms-audit-v1** - Consome eventos do Kafka e mantém um histórico de auditoria.
3. **ms-email-v1** - Consome eventos de criação de clientes e simula o envio de e-mails via logs.

### 🏗 Arquitetura
O sistema utiliza a **arquitetura Vexa**, uma variação da arquitetura hexagonal desenvolvida pela TL Vivo. Essa abordagem promove a separação de responsabilidades, garantindo maior flexibilidade e desacoplamento entre os componentes do sistema.

### 🔄 Fluxo de Comunicação
1. **Cliente realiza uma requisição HTTP** no ms-customer-v1 (criação, atualização ou exclusão de um cliente).
2. **ms-customer-v1 persiste os dados no banco** e publica um evento no Kafka.
3. **ms-audit-v1 consome o evento** e armazena os detalhes da ação para auditoria.
4. **ms-email-v1 consome o evento de criação de cliente** e registra um log simulando um e-mail de boas-vindas.

### 🛠 Tecnologias Utilizadas
- **Spring Boot** - Framework para desenvolvimento dos microsserviços.
- **Apache Kafka** - Ppara comunicação assíncrona.
- **PostgreSQL** - Banco de dados para persistência de dados.
- **Docker e Docker Compose** - Orquestração dos microsserviços.

### 🚀 Como Executar o Projeto
#### Pré-requisitos
* Docker e Docker Compose instalados.
