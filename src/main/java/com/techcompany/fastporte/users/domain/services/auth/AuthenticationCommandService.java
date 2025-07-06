package com.techcompany.fastporte.users.domain.services.auth;

import com.techcompany.fastporte.users.domain.model.commands.auth.AuthenticateAccountCommand;
import com.techcompany.fastporte.users.domain.model.commands.auth.UpdatePasswordCommand;

public interface AuthenticationCommandService {

    public String handle(AuthenticateAccountCommand command);
    public void handle(UpdatePasswordCommand command);
}