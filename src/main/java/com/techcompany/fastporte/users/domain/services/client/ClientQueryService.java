package com.techcompany.fastporte.users.domain.services.client;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.queries.client.CheckClientExistsByIdQuery;
import com.techcompany.fastporte.users.domain.model.queries.client.GetAllClientsQuery;
import com.techcompany.fastporte.users.domain.model.queries.client.GetClientByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ClientQueryService {
    Optional<Client> handle(GetClientByIdQuery query);
    List<Client> handle(GetAllClientsQuery query);

    Boolean handle(CheckClientExistsByIdQuery query);
}
