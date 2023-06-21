# RealTimeChat

![Java](https://img.shields.io/badge/Java-v17.0-blue)
![NodeJS](https://img.shields.io/badge/NodeJS-v18.0-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-v3.1.0-blue)
![ReactJS](https://img.shields.io/badge/ReactJS-v18.2-blue)
![MySQL](https://img.shields.io/badge/MySQL-v8.0.29-blue)

O RealTimeChat é uma aplicação de chat em tempo real que consiste em um frontend em ReactJS, um backend em Spring Boot e um servidor proxy reverso Nginx. A aplicação permite que os usuários se cadastrem, façam login e conversem em salas de chat.

## Pré-requisitos

- Docker e Docker Compose instalados na máquina.

## Como executar a aplicação

1. Clone o repositório para sua máquina local:

```
git clone https://github.com/tiduswr/RealtimeCHAT.git
```

2. Navegue até o diretório do projeto:

```
cd RealTimeChat
```

3. Atualize o arquivo "MODELO - docker-compose.override.yml" renomeando para "docker-compose.override.yml" e preenchendo com os dados sensíveis da aplicação.

````
version: '3.7'

services:
  database:
    environment:
      - MYSQL_USER=seu_database_user
      - MYSQL_PASSWORD=seu_password
      - MYSQL_ROOT_PASSWORD=seu_database_root_password

  backend:
    environment:
      - DB_USERNAME=seu_database_user
      - DB_PASSWORD=seu_password
      - JWT_SECRET=sua_chave_jwt_secret
````

4. Construa e execute os contêineres Docker usando o Docker Compose:

```
docker-compose up -d
```

5. Aguarde até que todos os contêineres sejam inicializados corretamente. Após a conclusão, você poderá acessar o frontend em `http://localhost` e começar a usar a aplicação de chat em tempo real.

## Estrutura do projeto

- `backend`: Contém o código-fonte e os arquivos de configuração do backend Spring Boot.
- `frontend`: Contém o código-fonte e os arquivos de configuração do frontend ReactJS.
- `database`: Contém os arquivos de configuração e scripts para a inicialização do banco de dados MySQL.
- `proxy`: Contém os arquivos de configuração do servidor de roteamento de requisições usando NGINX.

## Screenshots

![Sala de Bate Papo](readme_screenshots/conversas.png)

![Sala de Bate Papo](readme_screenshots/perfil.png)

![Sala de Bate Papo](readme_screenshots/busca.png)

## Contribuição

Contribuições são bem-vindas! Se você encontrar algum problema ou tiver sugestões de melhorias, fique à vontade para abrir uma issue ou enviar um pull request.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).