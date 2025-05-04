package com.techcompany.fastporte.trips.domain.model.queries;

public record GetTripsByClientIdAndStatusQuery(Long clientId, Long statusId) {
}
