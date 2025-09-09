package com.jug.demo.integrations;

import com.jug.demo.entities.ClientEntity;
import com.jug.demo.repositories.ClientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE) // Não substituir pelo banco em memória
@DisplayName("ClientRepository - Teste de integração com Testcontainers")
class ClientRepositoryIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        // Se quiser deixar explícito:
        // registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    ClientRepository clientRepository;

    @Test
    @DisplayName("deve salvar e recuperar um cliente por id")
    void shouldSaveAndFindById() {
        // arrange
        ClientEntity novo = new ClientEntity();
        novo.setName("Alice");
        // ... setar outros campos necessários

        // act
        ClientEntity salvo = clientRepository.save(novo);
        Optional<ClientEntity> encontrado = clientRepository.findById(salvo.getId());

        // assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getName()).isEqualTo("Alice");
    }

    @Test
    @DisplayName("deve listar clientes")
    void shouldListAll() {
        ClientEntity a = new ClientEntity();
        a.setName("A");
        clientRepository.save(a);

        ClientEntity b = new ClientEntity();
        b.setName("B");
        clientRepository.save(b);

        assertThat(clientRepository.findAll()).hasSizeGreaterThanOrEqualTo(2);
    }
}