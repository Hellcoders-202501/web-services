package com.techcompany.fastporte.trips.application.internal.queryservices;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllRequestsByClientIdAndNotTakenQuery;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllRequestsByServiceIdAndNotTakenQuery;
import com.techcompany.fastporte.trips.domain.model.queries.GetRequestByIdQuery;
import com.techcompany.fastporte.trips.domain.services.RequestQueryService;
import com.techcompany.fastporte.trips.infrastructure.persistence.jpa.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RequestQueryServiceImp implements RequestQueryService {

    private final RequestRepository requestRepository;

    public RequestQueryServiceImp(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    public List<Request> handle(GetAllRequestsByServiceIdAndNotTakenQuery query) {
        return requestRepository.findAllByService_IdAndStatus_StatusIsNot(query.serviceId(), RequestStatusType.TAKEN);
    }

    @Override
    public List<Request> handle(GetAllRequestsByClientIdAndNotTakenQuery query) {
        return List.of();
    }

    @Override
    public Optional<Request> handle(GetRequestByIdQuery query) {
        return requestRepository.findById(query.id());
    }
}
