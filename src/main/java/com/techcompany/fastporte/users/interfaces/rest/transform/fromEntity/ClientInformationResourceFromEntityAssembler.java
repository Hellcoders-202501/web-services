package com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.interfaces.rest.resources.ClientInformationResource;

public class ClientInformationResourceFromEntityAssembler {

    public static ClientInformationResource toResourceFromEntity(Client client) {
        return new ClientInformationResource(
                client.getId(),
                client.getUser().getName(),
                client.getUser().getFirstLastName(),
                client.getUser().getSecondLastName(),
                client.getUser().getEmail(),
                client.getUser().getPhone(),
                client.getUser().getDescription(),
                client.getUser().getId()
        );
    }
}
