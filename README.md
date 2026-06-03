# ImovCG - Backend

Backend da plataforma **ImovCG**, um agregador de anúncios de aluguel de imóveis. O sistema é responsável pelo gerenciamento, armazenamento e disponibilização dos imóveis coletados através de processos de *web scraping*.

## Funcionalidades Implementadas

O sistema conta atualmente com os seguintes recursos principais:

* **Cadastro e armazenamento de imóveis**
* **Consulta de imóveis por ID**
* **Listagem dinâmica com filtros**


Os filtros disponíveis atualmente incluem:

* Preço mínimo
* Preço máximo
* Tipo do imóvel (Casa, Apartamento, etc.)

---

## Tecnologias Utilizadas

* Java 21
* Spring Boot
* MySQL
* Maven

---

## Como Executar o Projeto Localmente

O projeto utiliza o **Maven Wrapper (`mvnw`)**, portanto não é necessário instalar o Maven globalmente.

### Pré-requisitos

* Java JDK 21 ou superior
* MySQL instalado e configurado
* IDE de sua preferência (VS Code, IntelliJ IDEA, Eclipse)

---

## Passo a Passo

### 1. Clone o repositório

```bash
git clone https://github.com/ImovCG/backend.git
cd imovcg-back
```

### 2. Configure o banco de dados

Crie um banco MySQL e ajuste as credenciais no arquivo:

```properties
src/main/resources/application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/imovcg
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Compile o projeto

#### Windows

```cmd
mvnw.cmd clean compile
```

#### Linux / macOS

```bash
chmod +x mvnw
./mvnw clean compile
```

### 4. Execute a aplicação

#### Windows

```cmd
mvnw.cmd spring-boot:run
```

#### Linux / macOS

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em:

```txt
http://localhost:8080
```

## Documentação da API (Swagger)

Após iniciar a aplicação, a documentação interativa da API pode ser acessada em:

```txt
http://localhost:8080/swagger-ui/index.html
```

### Estrutura do Projeto

```text
src
├── main
│   ├── java/com/imovcg/back
│   │   ├── config
│   │   ├── controller
│   │   ├── dto
│   │   ├── model
│   │   ├── repository
│   │   ├── service
│   │   └── specification
│   └── resources
└── test
```
---
Projeto desenvolvido para a disciplina de Projeto I.
