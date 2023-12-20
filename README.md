# RealtimeCHAT

![Java](https://img.shields.io/badge/Java-v17.0-blue)
![NodeJS](https://img.shields.io/badge/NodeJS-v18.0-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-v3.1.0-blue)
![ReactJS](https://img.shields.io/badge/ReactJS-v18.2-blue)
![MySQL](https://img.shields.io/badge/MySQL-v8.0.29-blue)

Este estudo consiste em uma análise comparativa entre as arquiteturas de software monolítica e de microsserviços em uma aplicação de chat, com o propósito de identificar diferenças nas estratégias de escalabilidade, eficiência no uso de recursos e impacto no tempo de resposta. A metodologia incluiu o desenvolvimento de uma aplicação monolítica e sua subsequente decomposição em microsserviços. Foram realizados testes de carga simulando cenários realistas, e os resultados destacaram que as estratégias de escalabilidade variam consideravelmente entre as arquiteturas. A arquitetura de microsserviços possibilitou o escalonamento automático de setores e funcionalidades específicas, conforme a demanda, demonstrando flexibilidade na gestão dos recursos. A eficiência de uso de recursos foi influenciada pela natureza das tarefas e características da carga de trabalho. Em relação à latência e ao tempo de resposta, a arquitetura monolítica superou a de microsserviços em algumas funcionalidades, enquanto a última se destacou em outras. Observou-se que o número de chamadas de APIs internas nas aplicações de microsserviços impactou diretamente a latência e o desempenho global. A pesquisa foi fundamentada principalmente nos estudos de Microsserviços de Martin Fowler, e, espera-se que a mesma possa contribuir para desenvolvedores e tomadores de decisão que buscam selecionar a arquitetura mais apropriada para suas aplicações, destacando áreas para futuras pesquisas.

[TCC (PDF)](https://drive.google.com/file/d/1QuAb9c41EtZGry348lFwD18zjFISJoF2/view?usp=sharing)

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
