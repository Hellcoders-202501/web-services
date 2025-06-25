package com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Service;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Vehicle;
import com.techcompany.fastporte.users.interfaces.rest.resources.DriverVehicleResource;

import java.util.stream.Collectors;

public class DriverVehicleResourceFromEntityAssembler {

    public static DriverVehicleResource toResourceFromEntity(Vehicle vehicle) {
        return new DriverVehicleResource(
                vehicle.getId(),
                vehicle.getBrand(),
                vehicle.getServices().stream()
                        .map(Service::getNameSpanish)
                        .collect(Collectors.toList()),
                vehicle.getImageUrl()
        );
    }
}
