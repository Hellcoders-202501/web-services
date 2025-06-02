package com.techcompany.fastporte.users.interfaces.rest.resources;

import java.util.List;

public record DriverVehicleResource(
        String brand,
        List<String> services,
        String imageUrl
) {
}
