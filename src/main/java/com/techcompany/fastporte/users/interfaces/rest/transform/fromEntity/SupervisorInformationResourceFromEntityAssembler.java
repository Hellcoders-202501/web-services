package com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.interfaces.rest.resources.SupervisorInformationResource;

public class SupervisorInformationResourceFromEntityAssembler {

    public static SupervisorInformationResource toResourceFromEntity(Supervisor supervisor) {
        return new SupervisorInformationResource(
                supervisor.getId(),
                supervisor.getUser().getName(),
                supervisor.getUser().getFirstLastName(),
                supervisor.getUser().getSecondLastName(),
                supervisor.getUser().getEmail(),
                supervisor.getUser().getPhone(),
                supervisor.getUser().getUsername()
        );
    }
}
