// Builder para ClientEntity
package com.jug.demo.entities;

public class ClientEntityBuilder {
    private Long id;
    private String name;
    private String email;

    public ClientEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ClientEntityBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ClientEntityBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ClientEntity build() {
        return new ClientEntity(id, name, email);
    }
}