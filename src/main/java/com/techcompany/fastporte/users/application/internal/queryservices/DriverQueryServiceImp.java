package com.techcompany.fastporte.users.application.internal.queryservices;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.queries.driver.*;
import com.techcompany.fastporte.users.domain.services.driver.DriverQueryService;
import com.techcompany.fastporte.users.infrastructure.persistence.jpa.DriverRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DriverQueryServiceImp implements DriverQueryService {

    private final DriverRepository driverRepository;

    public DriverQueryServiceImp(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public Optional<Driver> handle(GetDriverByIdQuery query) {
        return driverRepository.findById(query.id());
    }

    @Override
    public List<Driver> handle(GetAllDriversQuery query) {
        return driverRepository.findAll();
    }

    @Override
    public List<Driver> handle(GetAllDriversByIdInList query) {
        return driverRepository.findAllById(query.ids());
    }

    @Override
    public List<Driver> handle(GetAllDriversBySupervisorIdQuery query) {
        return driverRepository.findAllBySupervisor_Id(query.supervisorId());
    }

    @Override
    public Optional<Driver> handle(GetDriverPrivateProfileQuery query) {
        return driverRepository.findById(query.id());
    }

    @Override
    public Optional<Driver> handle(GetDriverPublicProfileQuery query) {
        return driverRepository.findById(query.id());
    }

    @Override
    public Boolean handle(CheckDriverExistsByIdQuery query) {
        return driverRepository.existsById(query.driverId());
    }
}
