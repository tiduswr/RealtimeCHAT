# RealTimeChat

![Java](https://img.shields.io/badge/Java-v17.0-blue)
![NodeJS](https://img.shields.io/badge/NodeJS-v18.0-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-v3.1.0-blue)
![ReactJS](https://img.shields.io/badge/ReactJS-v18.2-blue)
![MySQL](https://img.shields.io/badge/MySQL-v8.0.29-blue)

O RealTimeChat é uma aplicação de chat em tempo real que consiste em um frontend em ReactJS, um backend baseado em microserviços feito em Spring Boot junto de um servidor proxy reverso Nginx. A aplicação permite que os usuários se cadastrem, façam login e conversem em salas de chat.

## Pré-requisitos

- Docker;
- Docker-Compose;
- Minikube;
- Kubernetes;

## Como fazer deploy via Kubernetes

1. Clone o repositório para sua máquina local:

```
git clone https://github.com/tiduswr/RealtimeCHAT.git
```

2. Navegue até o diretório do projeto:

```
cd RealTimeCHAT
```

3. Atualize o arquivo "secrets_MODELO.yaml" renomeando para "secrets.yaml" e preenchendo com os dados sensíveis da aplicação.

````
apiVersion: v1
kind: Secret
metadata:
  name: secrets
  namespace: webchat
type: Opaque
data:
  #Precisa estar em base64
  db-user: 
  db-pass: 
  db-root-pass: 
  email-sender:
  smtp-user:
  smtp-password:
  jwt-secret: 
````

4. Construa na ordem abaixo os objetos:

```
$ kubectl create -f dependency/namespace/
$ kubectl create -f dependency/persistent_volume_claim/
$ kubectl create -f dependency/secrets/
$ kubectl create -f dependency/deployments/
$ kubectl create -f dependency/service/
$ kubectl create -f microservices/deployments/
$ kubectl create -f microservices/service/
```

5. Aguarde até que todos os contêineres sejam inicializados corretamente. Para verificar o status de cada um utilize:

```
$ kubectl get all -n webchat
```

6. Para verificar o link de acesso a aplicação utilize:

```
$ minikube service webchat-proxy -n webchat
```

7. Para exportar o serviço na rede local utilize(no linux):

```
ssh -i ~/.minikube/machines/minikube/id_rsa docker@$(minikube ip) -L \*:30001:0.0.0.0:30001
```

## Estrutura do projeto

- `microservices`: Contém o código-fonte de todos os serviços que compõem a aplicação.
- `deploy`: Possui toda a estrutura para funcionamento dos serviços via kubernetes.
- `frontend`: Contém o código-fonte e os arquivos de configuração do frontend ReactJS.
- `database`: Contém os arquivos de configuração e scripts para a inicialização do banco de dados MySQL.
- `proxy`: Contém os arquivos de configuração do servidor de roteamento de requisições usando NGINX.
- `backend`: Contém o código-fonte e os arquivos de configuração do backend monolitico em Spring Boot (código para testes).

## Screenshots

![Sala de Bate Papo](readme_screenshots/conversas.png)

![Sala de Bate Papo](readme_screenshots/perfil.png)

![Sala de Bate Papo](readme_screenshots/busca.png)

## Contribuição

Dado que o intuito desse projeto é ser para o meu TCC de Ciências da Computação, as contribuções serão permitidas apenas após a avaliação, pois até a data de entrega todo o trabalho precisa ser meu.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).