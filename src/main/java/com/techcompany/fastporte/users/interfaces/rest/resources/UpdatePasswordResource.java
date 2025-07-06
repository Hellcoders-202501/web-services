package com.techcompany.fastporte.users.interfaces.rest.resources;

public record UpdatePasswordResource(
        String email,
        String newPassword
) {
}
