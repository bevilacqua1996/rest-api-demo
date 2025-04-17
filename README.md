# 📘 Documentando uma API Spring Boot com SpringDoc OpenAPI

Este projeto demonstra como converter uma API REST padrão do Spring Boot para utilizar **SpringDoc OpenAPI** (Swagger UI) para documentação interativa dos endpoints.

---

## 🎯 Objetivo

- Adicionar o SpringDoc ao projeto
- Gerar documentação automática da API
- Enriquecer os endpoints com descrições usando anotações OpenAPI
- Acessar e testar os endpoints diretamente pela interface Swagger

---

## ✅ Pré-requisitos

- Projeto Spring Boot funcionando
- Java 17 ou superior
- Maven ou Gradle configurado

---

## 🚀 Passo a passo

### 1. Adicione a dependência no projeto

#### ➡️ Maven

```xml
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.5.0</version>
</dependency>
```

#### ➡️ Gradle

```groovy
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0'
```

---

### 2. Rode a aplicação

Acesse a documentação Swagger gerada automaticamente:

📎 `http://localhost:8080/swagger-ui.html`

---

### 3. Documente seus endpoints com anotações (opcional)

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Operações relacionadas a usuários")
public class UsuarioController {

    @GetMapping
    @Operation(
        summary = "Lista todos os usuários",
        description = "Retorna uma lista completa dos usuários cadastrados."
    )
    public List<UsuarioDTO> listar() {
        // lógica de listagem
    }
}
```

---

### 4. (Opcional) Personalize informações da documentação

```java
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Minha API")
                .version("1.0")
                .description("Documentação da API de usuários"));
    }
}
```

---

## ✅ Desafio prático

1. Utilize DTOs para transferencia de informação entre repository e controller
2. Valide os dados com `@Valid` no DTO
3. Documente o endpoint com `@Operation`
4. Teste no Swagger UI!

---

## 📌 Referência

- [SpringDoc OpenAPI Docs](https://springdoc.org/)
- [OpenAPI Specification](https://swagger.io/specification/)

---
