package com.techcompany.fastporte.users.domain.services.driver;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Comment;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Experience;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Vehicle;
import com.techcompany.fastporte.users.domain.model.queries.driver.*;

import java.util.List;
import java.util.Optional;

public interface DriverQueryService {
    Optional<Driver> handle(GetDriverByIdQuery query);
    List<Driver> handle(GetAllDriversQuery query);
    List<Driver> handle(GetAllDriversByIdInList query);

    Optional<Driver> handle(GetDriverPrivateProfileQuery query);
    Optional<Driver> handle(GetDriverPublicProfileQuery query);

    Boolean handle(CheckDriverExistsByIdQuery query);

    List<Experience> handle(GetAllExperiencesByDriverIdQuery query);
    List<Vehicle> handle(GetAllVehiclesByDriverIdQuery query);
    List<Comment> handle(GetAllCommentsByDriverIdQuery query);
    List<Driver> handle(GetMostRankedDriversQuery query);
}
