package com.techcompany.fastporte.shared.dtos;

public record ClientInformationDto(
        Long id,
        String name,
        String firstLastName,
        String secondLastName,
        String phone
) {
}
