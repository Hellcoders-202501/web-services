package com.techcompany.fastporte.trips.application.internal.queryservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Application;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllApplicationsByRequestIdQuery;
import com.techcompany.fastporte.trips.domain.services.ApplicationQueryService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationQueryServiceImp implements ApplicationQueryService {

    private final ApplicationRepository applicationRepository;

    public ApplicationQueryServiceImp(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Application> handle(GetAllApplicationsByRequestIdQuery query) {
        return applicationRepository.findByRequest_Id(query.requestId());
    }
}
