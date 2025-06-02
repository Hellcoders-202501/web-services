package com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.shared.resources.summary.ClientSummaryResource;
import com.techcompany.fastporte.shared.transform.ClientSummaryResourceFromEntityAssembler;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Comment;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.User;
import com.techcompany.fastporte.users.interfaces.rest.resources.DriverCommentResource;

public class DriverCommentResourceFromEntityAssembler {

    public static DriverCommentResource toResourceFromEntity(Comment comment) {

        //User user = comment.getTrip().getRequest().getClient().getUser();

        Client client = comment.getTrip().getRequest().getClient();

        return new DriverCommentResource(
                comment.getContent(),
                comment.getRating(),
                ClientSummaryResourceFromEntityAssembler.assemble(client)
        );
    }
}
