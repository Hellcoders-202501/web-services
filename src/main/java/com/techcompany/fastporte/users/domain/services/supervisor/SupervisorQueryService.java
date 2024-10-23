package com.techcompany.fastporte.users.domain.services.supervisor;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.domain.model.queries.supervisor.GetAllSupervisorsQuery;
import com.techcompany.fastporte.users.domain.model.queries.supervisor.GetSupervisorByIdQuery;

import java.util.List;
import java.util.Optional;

public interface SupervisorQueryService {
    Optional<Supervisor> handle(GetSupervisorByIdQuery query);
    List<Supervisor> handle(GetAllSupervisorsQuery query);
}
