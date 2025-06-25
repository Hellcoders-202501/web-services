package com.techcompany.fastporte.users.interfaces.rest.resources;

import java.math.BigDecimal;

public record DriverExperienceResource(
        Long id,
        String job,
        BigDecimal duration
) {
}
