// Builder para ClientResponse
package com.jug.demo.entities;

import com.jug.demo.generated.models.ClientResponse;

public class ClientResponseBuilder {
    private Integer id;
    private String name;
    private String email;

    public ClientResponseBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public ClientResponseBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ClientResponseBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ClientResponse build() {
        ClientResponse clientResponse = new ClientResponse();
        clientResponse.setId(this.id);
        clientResponse.setName(this.name);
        clientResponse.setEmail(this.email);
        return clientResponse;
    }
}