// Builder para ClientRequest
package com.jug.demo.entities;

import com.jug.demo.generated.models.ClientRequest;

public class ClientRequestBuilder {
    private Integer id;
    private String name;
    private String email;

    public ClientRequestBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public ClientRequestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ClientRequestBuilder email(String email) {
        this.email = email;
        return this;
    }

    public ClientRequest build() {
        ClientRequest clientRequest = new ClientRequest();
        clientRequest.setId(this.id);
        clientRequest.setName(this.name);
        clientRequest.setEmail(this.email);
        return clientRequest;
    }
}