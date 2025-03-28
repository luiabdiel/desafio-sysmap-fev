## 📌 Visão Geral

Este projeto consiste em um sistema distribuído de gestão de clientes com comunicação assíncrona via Apache Kafka. O sistema é composto por três microsserviços principais:

- **ms-customer-v1**: Responsável pelo CRUD de clientes, autenticação de usuários e publicação de eventos no Kafka.
- **ms-audit-v1**: Consome eventos do Kafka e mantém um histórico de auditoria.
- **ms-email-v1**: Consome eventos de criação de clientes e envia e-mails via Mailtrap.

Todos os microsserviços possuem **100% de cobertura de testes unitários**, garantindo qualidade e confiabilidade do sistema.

## 🏠 Arquitetura

O sistema utiliza a arquitetura **Vexa**, uma variação da arquitetura hexagonal desenvolvida pela Telefônica Vivo. Diferente da hexagonal tradicional, a Vexa permite o uso livre de anotações, como `@Service` na camada de serviço.

## 👍 Fluxo de Comunicação

1. Cliente realiza uma requisição HTTP no **ms-customer-v1** (criação, atualização ou exclusão de um cliente). Para isso, é necessário estar autenticado.
2. **ms-customer-v1** persiste os dados no banco e publica um evento no Kafka.
3. **ms-audit-v1** consome o evento e armazena os detalhes da ação para auditoria.
4. **ms-email-v1** consome o evento de criação de cliente e envia um e-mail de boas-vindas via Mailtrap.

## 🔐 Autenticação e Segurança

O **ms-customer-v1** implementa autenticação via **JWT (JSON Web Token)**. Para criar, listar, buscar por ID, atualizar e remover clientes, é necessário estar autenticado.

### Rotas de Autenticação

- `POST /api/v1/auth/register` - Registrar um novo usuário.
- `POST /api/v1/auth/login` - Autenticar um usuário e obter um token JWT.

Após o login, as requisições devem incluir o token JWT no cabeçalho `Authorization` como `Bearer <TOKEN>`

## 🛠️ Tecnologias Utilizadas

- **Spring Boot**: Framework para desenvolvimento dos microsserviços.
- **Apache Kafka**: Middleware para comunicação assíncrona.
- **PostgreSQL**: Banco de dados para persistência de dados.
- **Docker e Docker Compose**: Orquestração dos microsserviços.
- **Mailtrap**: Serviço para testar e-mails em ambiente de desenvolvimento.
- **JWT** (JSON Web Token): Autenticação segura.

## 🚀 Como Executar o Projeto

### **Pré-requisitos**

- Docker e Docker Compose instalados.
- Conta no Mailtrap (para configurar o envio de e-mails).

### **Passos para execução**

1. Clone este repositório:

   ```bash
   git clone https://github.com/luiabdiel/desafio-sysmap-fev.git
   cd desafio-sysmap-fev
   ```

2. Inicie os serviços com Docker Compose:

   ```bash
   docker-compose up -d
   ```

3. Acesse os serviços:

   - **ms-customer-v1**: [http://localhost:8080](http://localhost:8080)
   - **ms-audit-v1**: [http://localhost:8081](http://localhost:8081)
   - **ms-email-v1**: [http://localhost:8082](http://localhost:8082)


4. **Configuração do Mailtrap no ms-email-v1**:

   Para que o **ms-email-v1** utilize o Mailtrap, configure o arquivo `application.yml` com as credenciais fornecidas pelo Mailtrap:

   ```yaml
   mail:
     host: sandbox.smtp.mailtrap.io
     port: 2525
     username: <seu-username>
     password: <seu-password>
     properties:
       email:
         smtp:
           auth: true
           starttls:
             enable: true
   ```

   **Observação:** As credenciais para acessar o sistema do Mailtrap (para visualizar a caixa de e-mails) são as seguintes:
   
- **E-mail:** desafio.trainee.sysmap@gmail.com
- **Senha:** RoSr7@mfRoSr7@mf

  **Importante:** Essa conta do Mailtrap foi criada **especificamente para este projeto e não é uma conta pessoal**. Ela é destinada apenas para o envio e visualização dos e-mails de boas-vindas. As credenciais fornecidas são usadas para acessar o sistema do Mailtrap e visualizar os e-mails na caixa de e-mails.

  **Atenção:**
  Para acessar o Mailtrap, recomendamos que você faça login na conta do Gmail primeiro e, em seguida, acesse o Mailtrap. Isso ocorre porque, ao tentar entrar diretamente com e-mail e senha, o Mailtrap pode solicitar um código de verificação. Como as credenciais do Mailtrap são as mesmas da conta do Gmail, basta logar no Gmail primeiro e depois acessar o Mailtrap normalmente.

## 💍 Endpoints Principais

### **ms-customer-v1 (Gestão de Clientes - Requer Autenticação)**

- `POST /api/v1/customers` - Criar um cliente.
- `GET /api/v1/customers` - Listar todos os clientes.
- `GET /api/v1/customers/{id}` - Buscar um cliente por ID.
- `PUT /api/v1/customers/{id}` - Atualizar um cliente.
- `DELETE /api/v1/customers/{id}` - Remover um cliente.

### **ms-audit-v1 (Auditoria)**

- `GET /api/v1/audits` - Listar logs de auditoria.

### **ms-email-v1 (Notificações)**

- Envio de e-mail de boas-vindas ao criar um cliente via Mailtrap.

## 📑 Swagger para Documentação de Endpoints
Os microsserviços **ms-audit-v1** e **ms-customer-v1** possuem documentação pelo Swagger.

- **ms-customer-v1** - http://localhost:8080/api/v1/swagger-ui/index.html#/
- **ms-audit-v1** - http://localhost:8081/api/v1/swagger-ui/index.html#/

## 📚 Evidências
As evidências do projeto estão documentadas na [Wiki do repositório](https://github.com/luiabdiel/desafio-sysmap-fev/wiki/Sistema-de-Gest%C3%A3o-de-Clientes-com-Kafka).

## 💡 Dúvidas ou sugestões?

Entre em contato!

