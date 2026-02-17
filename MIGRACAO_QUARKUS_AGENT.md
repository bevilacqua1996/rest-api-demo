# 🤖 Como construir um **custom agent** para migrar esta aplicação de Spring Boot para Quarkus

Este guia propõe um agente focado em migração incremental, com segurança e entregas pequenas.

## 1) Objetivo do agente

Criar um agente que:
- leia o código atual em `demo/src/main/java`;
- proponha e aplique mudanças de Spring para Quarkus em etapas;
- preserve contratos REST existentes;
- execute validações automáticas a cada etapa;
- gere documentação da migração (o que mudou e por quê).

---

## 2) Escopo desta aplicação (inventário inicial)

O agente deve começar com um inventário automático dos componentes atuais:
- Controllers REST (`demo/src/main/java/com/jug/demo/controllers`)
- Services (`demo/src/main/java/com/jug/demo/services`)
- Repository (`demo/src/main/java/com/jug/demo/repositories`)
- Entity e Mapper (`demo/src/main/java/com/jug/demo/entities`, `mappers`)
- Configuração OpenAPI (`demo/src/main/java/com/jug/demo/config/OpenApiConfig.java`)
- Configs e especificação (`demo/src/main/resources/application.yml`, `openapi.yaml`)

Com base nisso, ele define o plano de migração por camadas.

---

## 3) Arquitetura recomendada do custom agent

### 3.1. Perfil do agente

- **Nome**: `quarkus-migration-agent`
- **Especialidade**: Java backend, Spring Boot → Quarkus
- **Modo de trabalho**: incremental + test-first

### 3.2. Ferramentas que o agente deve usar

1. **Análise estática**
   - localizar anotações Spring (`@RestController`, `@Service`, `@Repository`, `@Autowired`, etc.)
2. **Refatoração guiada**
   - aplicar mapeamentos de API framework a framework
3. **Build e testes**
   - executar `mvn test` e checks de compilação
4. **Diff + relatório**
   - registrar mudanças por arquivo e risco

### 3.3. Memória/contexto do agente

- Contratos públicos (endpoints, payloads, status HTTP)
- Regras de negócio críticas
- Decisões arquiteturais tomadas durante migração

---

## 4) Mapa de migração (Spring → Quarkus)

O agente deve seguir uma tabela de tradução (playbook):

- `@RestController` + `@RequestMapping` → `@Path` + `@Produces/@Consumes`
- `@GetMapping/@PostMapping/...` → `@GET/@POST/...` (JAX-RS)
- `ResponseEntity<T>` → `Response` (ou retorno direto com código explícito quando necessário)
- `@Service`/`@Component` → `@ApplicationScoped`
- Injeção por `@Autowired` → `@Inject`
- Spring Data JPA → Panache Repository/Entity (ou JPA padrão no Quarkus)
- `application.yml` Spring → `application.properties`/`application.yml` do Quarkus
- SpringDoc → extensão OpenAPI do Quarkus (`smallrye-openapi`)

> Regra do agente: migrar **um conjunto por vez** (ex.: primeiro controller, depois service, etc.).

---

## 5) Workflow do agente em fases

### Fase A — Descoberta
- mapear endpoints atuais e DTOs;
- capturar comportamento esperado (incluindo códigos HTTP).

### Fase B — Bootstrap Quarkus
- criar módulo/projeto Quarkus base;
- adicionar extensões necessárias (REST, Jackson, Hibernate, OpenAPI, JDBC).

### Fase C — Migração por vertical
Para cada feature (ex.: `Client`):
1. migrar endpoint;
2. migrar service;
3. migrar persistência;
4. ajustar validações/mapeadores;
5. executar testes.

### Fase D — Compatibilidade
- comparar OpenAPI atual x OpenAPI nova;
- validar se contratos críticos foram preservados.

### Fase E — Hardening
- revisar performance, observabilidade e configuração;
- limpar dependências Spring remanescentes.

---

## 6) Prompt base do seu custom agent

Use este prompt como base:

```text
Você é um agente especialista em migração de Spring Boot para Quarkus.
Objetivo: migrar esta aplicação em passos pequenos, mantendo compatibilidade de API.
Regras:
1) Nunca faça mudanças grandes sem quebrar em etapas.
2) Antes de alterar código, liste impacto por arquivo.
3) Preserve contratos REST (path, payload, status) salvo instrução contrária.
4) Após cada etapa, rode build/testes e reporte resultados.
5) Gere um resumo final: decisões, riscos, próximos passos.
```

---

## 7) Critérios de aceite automáticos do agente

O agente só conclui a migração quando:
- build compila sem dependências Spring não usadas;
- testes passam;
- endpoints principais respondem conforme contrato;
- documentação OpenAPI é gerada e acessível;
- relatório final de migração é produzido.

---

## 8) Estratégia prática para este repositório

Sequência sugerida:
1. Migrar `ClientController` para JAX-RS no Quarkus.
2. Migrar `ClientService` e injeção CDI.
3. Migrar `ClientRepository` para abordagem Quarkus (Panache ou JPA).
4. Ajustar `OpenApiConfig` para padrão Quarkus.
5. Portar configurações de `application.yml`.
6. Executar testes, ajustar e remover restos de Spring.

---

## 9) Boas práticas do agente

- Criar PRs pequenos por fase.
- Sempre incluir rollback plan.
- Evitar “big bang migration”.
- Priorizar compatibilidade externa antes de otimizações internas.

---

## 10) Próximo passo sugerido

Se quiser, o próximo incremento é eu transformar este guia em:
- checklist executável da migração;
- template de issue/PR para cada fase;
- prompt avançado com regras específicas deste projeto.
