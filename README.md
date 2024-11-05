## 🛠 Serviço de Login

Este projeto tem como objetivo criar um serviço de login inicial que utiliza autenticação básica e permite operações essenciais para o usuário. Abaixo está uma visão geral das principais funcionalidades e tecnologias utilizadas.

### 📋 Objetivos e Funcionalidades

- **Autenticação de Usuário**: Utiliza o [Auth0](https://auth0.com/) para autenticação segura e gerenciamento de usuários.
- **Criação e Atualização de Conta**: Permite ao usuário criar uma conta e atualizar dados pessoais de forma segura.
- **Validação de Conta**: Inclui uma etapa de verificação para garantir a autenticidade e validade dos usuários.
- **Integração com Serviço de E-mail**: Este serviço será posteriormente integrado ao módulo de envio de e-mails para notificação e confirmação de ações dos usuários.

### 🧩 Tecnologias Utilizadas

- **Spring Security** e **Spring Data**: Utilizados para implementar autenticação, autorização e persistência de dados de forma segura.
- **Swagger**: Documentação da API para facilitar a visualização e interação com os endpoints.
- **RabbitMQ**: Mensageria que conecta o serviço de login com o serviço de e-mail.

### 🔍 Melhorias Futuras

Ainda há algumas melhorias planejadas, incluindo:

- Finalizar a integração completa com o serviço de e-mail.
- Refinar a documentação e os fluxos de autenticação para melhorar a experiência do usuário.

### 💡 Ideação

Abaixo está uma imagem que ilustra a ideação inicial do projeto com o fluxo de login e validação de usuário:

![Ideação](https://drive.google.com/uc?id=15MUrrGqOUIxM1uFra-VWud8h_A21QXYd)

### 🎯 Aprendizado

Este projeto foi uma oportunidade para me aprofundar em conceitos essenciais, como:

- **Spring Security**: Para garantir a segurança no acesso e na atualização dos dados dos usuários.
- **Spring Data**: Para facilitar a persistência e manipulação dos dados de maneira eficiente.

