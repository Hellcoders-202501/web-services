package com.techcompany.fastporte.trips.application.internal.queryservices;

import com.techcompany.fastporte.trips.domain.model.queries.GetAllServiceTypesQuery;
import com.techcompany.fastporte.trips.domain.services.ServiceQueryService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ServiceQueryServiceImp implements ServiceQueryService {

    private final ServiceRepository serviceRepository;

    public ServiceQueryServiceImp(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public List<com.techcompany.fastporte.trips.domain.model.aggregates.entities.Service> handle(GetAllServiceTypesQuery query) {
        return serviceRepository.findAll();
    }
}
