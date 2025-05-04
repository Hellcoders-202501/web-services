package com.techcompany.fastporte.users.domain.services.client;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.commands.client.DeleteClientCommand;
import com.techcompany.fastporte.users.domain.model.commands.client.RegisterClientCommand;
import com.techcompany.fastporte.users.domain.model.commands.client.UpdateClientInformationCommand;

import java.util.Optional;

public interface ClientCommandService {
    Optional<Client> handle(RegisterClientCommand command);
    Optional<Client> handle(UpdateClientInformationCommand command);
    void handle(DeleteClientCommand command);
}
