package com.techcompany.fastporte.shared.dtos;

public record DriverInformationDto(
        Long id,
        String name,
        String firstLastName,
        String secondLastName,
        String phone
) {
}
