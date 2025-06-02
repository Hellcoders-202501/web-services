package com.techcompany.fastporte.trips.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTripResource(
        @NotBlank(message = "El origen es requerido") String origin,
        @NotBlank(message = "El destino es requerido") String destination,
        @NotBlank(message = "La fecha es requerida") String date,
        @NotBlank(message = "La hora de inicio es requerida") String startTime,
        @NotBlank(message = "La hora de fin es requerida") String endTime,
        @NotBlank(message = "El monto (S/.) es requerido") Double amount,
        @NotBlank(message = "El asunto es requerido") String subject,
        String description
) {
}
