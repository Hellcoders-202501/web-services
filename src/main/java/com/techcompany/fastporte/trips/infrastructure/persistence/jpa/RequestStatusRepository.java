package com.techcompany.fastporte.trips.infrastructure.persistence.jpa;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.RequestStatus;
import com.techcompany.fastporte.trips.domain.model.aggregates.enums.RequestStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestStatusRepository extends JpaRepository<RequestStatus, Long> {

    Optional<RequestStatus> findByStatusEquals(RequestStatusType status);

}
