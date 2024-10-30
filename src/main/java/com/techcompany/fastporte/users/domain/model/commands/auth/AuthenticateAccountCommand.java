package com.techcompany.fastporte.users.domain.model.commands.auth;

public record AuthenticateAccountCommand(
        String email,
        String password
) {
}
