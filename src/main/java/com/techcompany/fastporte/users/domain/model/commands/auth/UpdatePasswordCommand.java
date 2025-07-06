package com.techcompany.fastporte.users.domain.model.commands.auth;

public record UpdatePasswordCommand(
        String email,
        String newPassword
) {
}
