package com.jug.demo.demo.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "demo_entity_spring")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;


    public ClientEntity() {

    }

    public ClientEntity(long l, String testEntity, String testSurname) {
        this.id = l;
        this.name = testEntity;
        this.surname = testSurname;
    }

    public void ClientEntity(long l, String s) {
        this.id = l;
        this.name = s;
    }

    public void ClientEntity() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}