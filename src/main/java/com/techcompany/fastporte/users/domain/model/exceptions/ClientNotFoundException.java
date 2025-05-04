package com.techcompany.fastporte.users.domain.model.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long clientId) {
        super("Error: The client with id " + clientId + " does not exist");
    }
}