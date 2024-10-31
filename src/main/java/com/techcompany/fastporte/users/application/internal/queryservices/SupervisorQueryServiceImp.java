package com.techcompany.fastporte.users.application.internal.queryservices;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.domain.model.queries.supervisor.CheckSupervisorExistsByIdQuery;
import com.techcompany.fastporte.users.domain.model.queries.supervisor.GetAllSupervisorsQuery;
import com.techcompany.fastporte.users.domain.model.queries.supervisor.GetSupervisorByIdQuery;
import com.techcompany.fastporte.users.domain.services.supervisor.SupervisorQueryService;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.SupervisorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SupervisorQueryServiceImp implements SupervisorQueryService {

    private final SupervisorRepository supervisorRepository;

    public SupervisorQueryServiceImp(SupervisorRepository supervisorRepository) {
        this.supervisorRepository = supervisorRepository;
    }

    @Override
    public Optional<Supervisor> handle(GetSupervisorByIdQuery query) {
        return supervisorRepository.findById(query.id());
    }

    @Override
    public List<Supervisor> handle(GetAllSupervisorsQuery query) {
        return supervisorRepository.findAll();
    }

    @Override
    public Boolean handle(CheckSupervisorExistsByIdQuery query) {
        return supervisorRepository.existsById(query.supervisorId());
    }
}
