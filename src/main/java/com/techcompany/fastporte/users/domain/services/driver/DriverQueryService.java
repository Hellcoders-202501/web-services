package com.techcompany.fastporte.users.domain.services.driver;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
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
}
