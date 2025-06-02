package com.techcompany.fastporte.shared.transform;

import com.techcompany.fastporte.shared.resources.summary.ClientSummaryResource;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;

public class ClientSummaryResourceFromEntityAssembler {

    public static ClientSummaryResource assemble(Client client) {

        return new ClientSummaryResource(
                client.getId(),
                client.getUser().getName(),
                client.getUser().getFirstLastName(),
                client.getUser().getImageUrl(),
                client.getUser().getPhone()
        );
    }
}
