package com.techcompany.fastporte.users.domain.model.exceptions;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Error: Invalid credentials");
    }
}
