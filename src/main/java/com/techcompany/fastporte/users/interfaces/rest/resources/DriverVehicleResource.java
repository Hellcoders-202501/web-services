package com.techcompany.fastporte.users.interfaces.rest.resources;

import java.util.List;

public record DriverVehicleResource(
        Long id,
        String brand,
        List<String> services,
        String imageUrl
) {
}
