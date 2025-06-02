package com.techcompany.fastporte.users.interfaces.rest.resources;

public record AddDriverExperienceResource(
        String job,
        Double duration,
        Long id
) {
}