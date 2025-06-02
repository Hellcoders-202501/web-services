package com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.shared.resources.summary.ClientSummaryResource;
import com.techcompany.fastporte.shared.transform.ClientSummaryResourceFromEntityAssembler;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Contract;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.interfaces.rest.resources.RequestResource;
import com.techcompany.fastporte.trips.interfaces.rest.resources.ServiceResource;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;

public class RequestResourceFromEntityAssembler {
    public static RequestResource toResourceFromEntity(Request request) {

        Client client = request.getClient();
        User clientUser = client.getUser();
        Trip trip = request.getTrip();
        Contract contract = request.getContract();

        return new RequestResource(
                request.getId(),
                ClientSummaryResourceFromEntityAssembler.assemble(client),
                new ServiceResource(request.getService().getId(), request.getService().getNameSpanish()),
                request.getStatus().getStatus().name(),
                TripInformationResourceFromEntityAssembler.toResourceFromEntity(trip),
                ContractResourceFromEntityAssembler.toResourceFromEntity(contract)
        );
    }
}
