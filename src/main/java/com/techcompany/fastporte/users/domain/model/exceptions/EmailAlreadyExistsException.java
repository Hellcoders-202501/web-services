package com.techcompany.fastporte.users.domain.model.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super("Error: The email '" + email + "' is already in use");
    }
}