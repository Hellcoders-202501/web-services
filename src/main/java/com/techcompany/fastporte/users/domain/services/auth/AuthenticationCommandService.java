package com.techcompany.fastporte.users.domain.services.auth;

import com.techcompany.fastporte.users.domain.model.commands.auth.AuthenticateAccountCommand;

public interface AuthenticationCommandService {

    public String handle(AuthenticateAccountCommand command);
}