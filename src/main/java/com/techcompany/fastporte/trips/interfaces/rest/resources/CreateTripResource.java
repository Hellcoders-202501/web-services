package com.techcompany.fastporte.trips.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record CreateTripResource(
        @NotBlank(message = "Driver ID is required") Long driverId,
        @NotBlank(message = "Supervisor ID is required") Long supervisorId,
        @NotBlank(message = "Origin is required") String origin,
        @NotBlank(message = "Destination is required") String destination,
        @NotBlank(message = "Type is required") String type,
        @NotBlank(message = "Amount is required") String amount,
        @NotBlank(message = "Weight is required") String weight,
        @NotBlank(message = "Date is required") String date,
        @NotBlank(message = "Start time is required") String startTime,
        @NotBlank(message = "End time is required") String endTime,
        String subject,
        String description
) {
}
