package com.techcompany.fastporte.users.interfaces.rest.resources;

public record AddDriverVehicleResource(
        String brand,
        String imageUrl,
        Long serviceId,
        Long driverId
) {
}
