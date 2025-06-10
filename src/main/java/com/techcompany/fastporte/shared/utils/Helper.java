package com.techcompany.fastporte.shared.utils;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Helper {

    public static List<Long> VerifyOverlappingRequests(List<Trip> tripsList, LocalDateTime requestStart, LocalDateTime requestEnd) {
        List<Long> overlappingRequestIds = new ArrayList<>();

        for (Trip trip : tripsList) {
            LocalDateTime tripStart = LocalDateTime.of(trip.getDate(), LocalTime.parse(trip.getStartTime()));
            LocalDateTime tripEnd = LocalDateTime.of(trip.getDate(), LocalTime.parse(trip.getEndTime()));

            if (!tripEnd.isBefore(requestStart) && !tripStart.isAfter(requestEnd)) {
                overlappingRequestIds.add(trip.getRequest().getId());
            }
        }
        return overlappingRequestIds;
    }
}
