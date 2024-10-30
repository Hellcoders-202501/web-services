package com.techcompany.fastporte.users.domain.model.exceptions;

public class SupervisorNotFoundException extends RuntimeException {
    public SupervisorNotFoundException(Long supervisorId) {
        super("Error: The supervisor with id " + supervisorId + " does not exist");
    }
}