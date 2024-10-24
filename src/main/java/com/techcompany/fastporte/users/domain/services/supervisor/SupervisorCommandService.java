package com.techcompany.fastporte.users.domain.services.supervisor;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.domain.model.commands.supervisor.DeleteSupervisorCommand;
import com.techcompany.fastporte.users.domain.model.commands.supervisor.RegisterSupervisorCommand;

import java.util.Optional;

public interface SupervisorCommandService {
    Optional<Supervisor> handle(RegisterSupervisorCommand command);
    void handle(DeleteSupervisorCommand command);
}
