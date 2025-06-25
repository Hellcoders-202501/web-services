package com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Experience;
import com.techcompany.fastporte.users.interfaces.rest.resources.DriverExperienceResource;

public class DriverExperienceResourceFromEntityAssembler {

    public static DriverExperienceResource toResourceFromEntity(Experience experience) {
        return new DriverExperienceResource(
                experience.getId(),
                experience.getJob(),
                experience.getDuration()
        );
    }

}
