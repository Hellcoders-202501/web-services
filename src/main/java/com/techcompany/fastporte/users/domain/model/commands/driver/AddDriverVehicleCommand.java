package com.techcompany.fastporte.users.domain.model.commands.driver;

public record AddDriverVehicleCommand(
        String brand,
        String imageUrl,
        Long serviceId,
        Long driverId
) {
}
