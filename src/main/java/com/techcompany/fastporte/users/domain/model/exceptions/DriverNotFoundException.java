package com.techcompany.fastporte.users.domain.model.exceptions;

public class DriverNotFoundException extends RuntimeException{
    public DriverNotFoundException(Long id) {
        super("Error: The driver with id '" + id + "' does not exist");
    }
}
