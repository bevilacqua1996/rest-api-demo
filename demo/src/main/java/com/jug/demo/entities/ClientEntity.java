package com.jug.demo.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @ManyToMany
    @JoinTable(
            name = "client_product",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<ProductEntity> products;


    public ClientEntity() {

    }

    public ClientEntity(long l, String testEntity, String testSurname) {
        this.id = l;
        this.name = testEntity;
        this.email = testSurname;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}