package com.techcompany.fastporte.users.interfaces.rest.transform.fromResource;

import com.techcompany.fastporte.users.domain.model.commands.driver.AddDriverExperienceCommand;
import com.techcompany.fastporte.users.interfaces.rest.resources.AddDriverExperienceResource;

public class AddDriverExperienceCommandFromResourceAssembler {
    public static AddDriverExperienceCommand toCommandFromResource(AddDriverExperienceResource resource) {
        return new AddDriverExperienceCommand(
                resource.job(),
                resource.duration(),
                resource.id()
        );
    }
}
