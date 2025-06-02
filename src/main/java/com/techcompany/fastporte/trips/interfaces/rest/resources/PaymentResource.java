package com.techcompany.fastporte.trips.interfaces.rest.resources;

public record PaymentResource(
        Long id,
        double amount,
        String status
) {
}
