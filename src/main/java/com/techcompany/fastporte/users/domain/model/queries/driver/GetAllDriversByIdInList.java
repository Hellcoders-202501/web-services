package com.techcompany.fastporte.users.domain.model.queries.driver;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record GetAllDriversByIdInList(
        @NotBlank(message = "The driver id list is required") List<Long> ids) {
}
