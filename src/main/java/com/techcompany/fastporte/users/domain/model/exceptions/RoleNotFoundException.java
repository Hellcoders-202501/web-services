package com.techcompany.fastporte.users.domain.model.exceptions;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String role) {
        super("Error: The role " + role + " does not exist");
    }
}