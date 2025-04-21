package com.jug.demo.exceptions;

public class ClientNotFoundException extends RuntimeException  {

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(Integer id) {
        super("Client with ID " + id + " not found");
    }

    public ClientNotFoundException() {
        super("Client not found");
    }
}