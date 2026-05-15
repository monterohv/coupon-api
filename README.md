# Coupon API 🎟️

API RESTful para gerenciamento de cupons de desconto, desenvolvida com Java 17 e Spring Boot 3.5.x.
O projeto foi criado com foco em boas práticas de desenvolvimento backend, regras de negócio robustas, validações de domínio e total portabilidade utilizando Docker.

---

# 🚀 Tecnologias Utilizadas

* Java 17
* Spring Boot 3.5.x
* Spring Data JPA
* Hibernate 6
* H2 Database
* Swagger / OpenAPI 3
* Docker
* Docker Compose
* Maven
* JUnit 5

---

# 📋 Funcionalidades

* Cadastro de cupons
* Atualização de cupons
* Exclusão lógica (Soft Delete)
* Busca de cupons
* Controle de resgate (`redeemed`)
* Sanitização automática do código do cupom
* Validações completas de negócio
* Documentação interativa com Swagger
* Persistência de dados via H2 File Mode

---

# 📌 Regras de Negócio

## UUID como Identificador

Todos os cupons utilizam UUID como identificador único.

Exemplo:

```text
cef9d1e3-aae5-4ab6-a297-358c6032b1e7
```

---

## Sanitização do Código

Caracteres especiais enviados no código do cupom são removidos automaticamente.

Exemplo:

```text
Entrada: CUP@#12!
Saída: CUP12
```

Após a sanitização, o sistema valida se o código possui exatamente:

* 6 caracteres
* Apenas caracteres alfanuméricos

---

## Validação de Desconto

O desconto mínimo permitido é:

```text
0.5
```

Valores abaixo disso são rejeitados pela aplicação.

---

## Data de Expiração

A data de expiração do cupom deve obrigatoriamente estar no futuro.

---

## Soft Delete

A aplicação utiliza exclusão lógica através de `@SoftDelete`.

Isso significa que:

* Os registros permanecem no banco
* Não aparecem em consultas comuns
* Mantêm integridade referencial

---

## Campo Redeemed

O campo booleano `redeemed` controla se o cupom já foi utilizado.

Exemplo:

```json
{
  "redeemed": true
}
```

---

# 🐳 Executando com Docker

## Pré-requisitos

* Docker Desktop instalado e iniciado

---

## Subindo a aplicação

Na raiz do projeto execute:

```bash
docker-compose up --build -d
```

---

# 🗄️ Banco de Dados H2

O banco está configurado em modo persistente (File Mode).

## Configuração JDBC

```text
jdbc:h2:file:/app/data/coupondb
```

## Credenciais

| Campo   | Valor |
| ------- | ----- |
| Usuário | sa    |
| Senha   | vazio |

---

## Console Web H2

Acesse:

```text
http://localhost:8080/h2-console
```

---

## Persistência Local

Os dados são armazenados localmente em:

```text
./h2-data
```

---

# 📖 Swagger / OpenAPI

Documentação interativa disponível em:

```text
http://localhost:8080/swagger-ui/index.html
```

Através dela é possível:

* Testar endpoints
* Visualizar contratos
* Explorar requests e responses

---

# 🧪 Testes

A aplicação possui mais de 80% de cobertura das regras de negócio através de testes automatizados.

## Executar testes

```bash
mvn test
```

---

# 📂 Estrutura do Projeto

```text
src
 ┣ main
 ┃ ┣ java
 ┃ ┃ ┗ com.lucas.couponapi
 ┃ ┃    ┣ controller
 ┃ ┃    ┣ service
 ┃ ┃    ┣ repository
 ┃ ┃    ┣ dto
 ┃ ┃    ┣ model
 ┃ ┃    ┣ exception
 ┃ ┃    ┃ ┗ handler
 ┃ ┃    ┣ config
 ┃ ┃    ┣ mapper
 ┃ ┃    ┗ CouponApiApplication.java
 ┃ ┗ resources
 ┗ test
    ┗ java
```

---

# 🔥 Diferenciais do Projeto

* Arquitetura organizada
* Clean Code
* Regras de domínio centralizadas
* Soft Delete
* Sanitização automática
* Persistência real do H2
* Totalmente containerizado
* API documentada
* Cobertura de testes elevada

---

# 👨‍💻 Autor

Desenvolvido por Lucas Monteiro.
