package com.techcompany.fastporte.users.interfaces.rest;

import com.techcompany.fastporte.shared.exception.ErrorResponse;
import com.techcompany.fastporte.shared.transform.DriverSummaryResourceFromEntityAssembler;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Comment;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Experience;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.UserDetailsImp;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Vehicle;
import com.techcompany.fastporte.users.domain.model.commands.driver.DeleteDriverCommand;
import com.techcompany.fastporte.users.domain.model.commands.driver.DeleteDriverExperienceCommand;
import com.techcompany.fastporte.users.domain.model.commands.driver.DeleteDriverVehicleCommand;
import com.techcompany.fastporte.users.domain.model.queries.driver.*;
import com.techcompany.fastporte.users.domain.services.driver.DriverCommandService;
import com.techcompany.fastporte.users.domain.services.driver.DriverQueryService;
import com.techcompany.fastporte.users.interfaces.rest.resources.*;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity.DriverCommentResourceFromEntityAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity.DriverExperienceResourceFromEntityAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity.DriverInformationResourceFromEntityAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity.DriverVehicleResourceFromEntityAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromResource.AddDriverExperienceCommandFromResourceAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromResource.AddDriverVehicleCommandFromResourceAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromResource.RegisterDriverCommandFromResourceAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromResource.UpdateDriverInformationCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/drivers")
@Tag(name = "Driver Management", description = "Operations for managing drivers, including creation, updating, and retrieval")
public class DriverController {
    private final DriverCommandService driverCommandService;
    private final DriverQueryService driverQueryService;

    public DriverController(DriverCommandService driverCommandService, DriverQueryService driverQueryService) {
        this.driverCommandService = driverCommandService;
        this.driverQueryService = driverQueryService;
    }

    /*
     * =================  DRIVER INFO SERVICES  =================
     */

    @Operation(summary = "Get a driver by ID", description = "Retrieves the details of a specific driver by their ID.")
    @PreAuthorize("hasRole('ROLE_DRIVER') or hasRole('ROLE_CLIENT') or hasRole('ROLE_ADMIN') ")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImp userDetails) {

        try {

            Optional<Driver> driver = driverQueryService.handle(new GetDriverByIdQuery(id));

            if (driver.isPresent()) {
                return ResponseEntity.ok(DriverInformationResourceFromEntityAssembler.toPublicResourceFromEntity(driver.get()));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get all drivers", description = "Retrieves a list of all drivers.")
    @PreAuthorize("hasRole('ROLE_DRIVER') or hasRole('ROLE_CLIENT') or hasRole('ROLE_ADMIN') ")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        try{
            List<Driver> drivers = driverQueryService.handle(new GetAllDriversQuery());

            if (drivers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {

                var driverInformationResources = drivers.stream()
                        .map(DriverInformationResourceFromEntityAssembler::toPublicResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/batch")
    @Operation(summary = "Get drivers in batch", description = "Retrieves a list of drivers by their IDs.")
    public ResponseEntity<List<DriverInformationResource>> findAllByIdIn(@RequestBody List<Long> driverIds) {
        List<Driver> drivers = driverQueryService.handle(new GetAllDriversByIdInList(driverIds));

        var driverInformationResources = drivers.stream()
                .map(DriverInformationResourceFromEntityAssembler::toPublicResourceFromEntity)
                .toList();

        return ResponseEntity.ok(driverInformationResources);
    }

    @Operation(summary = "Create a new driver", description = "Creates a new driver with the provided details.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody RegisterDriverResource resource) {

        Optional<Driver> driver = driverCommandService.handle(RegisterDriverCommandFromResourceAssembler.toCommandFromResource(resource));
        return driver.map(DriverInformationResourceFromEntityAssembler::toPublicResourceFromEntity)
                .map(driverInformationResource -> ResponseEntity.status(HttpStatus.CREATED).body(driverInformationResource))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
    }

    @Operation(summary = "Update driver information", description = "Updates the details of a driver.")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody UpdateDriverInformationResource resource) {

        Optional<Driver> driver = driverCommandService.handle(UpdateDriverInformationCommandFromResourceAssembler.toCommandFromResource(resource));
        return driver.map(DriverInformationResourceFromEntityAssembler::toPublicResourceFromEntity)
                .map(driverInformationResource -> ResponseEntity.status(HttpStatus.OK).body(driverInformationResource))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
    }

    @Operation(summary = "Delete a driver by ID", description = "Deletes the driver with the specified ID.")
    @PreAuthorize("hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (driverQueryService.handle(new GetDriverByIdQuery(id)).isPresent()) {
                driverCommandService.handle(new DeleteDriverCommand(id));

                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    /*
     * =================  DRIVER EXPERIENCE SERVICES  =================
     */

    @GetMapping("/{driverId}/experience/")
    public ResponseEntity<?> getAllExperiencesByDriverId(@PathVariable Long driverId) {
        try {

            List<Experience> experiences = driverQueryService.handle(new GetAllExperiencesByDriverIdQuery(driverId));

            if (experiences.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                var driverExperienceResources = experiences.stream()
                        .map(DriverExperienceResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverExperienceResources);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/experience")
    public ResponseEntity<?> addExperience(@Valid @RequestBody AddDriverExperienceResource resource) {
        try {

            driverCommandService.handle(AddDriverExperienceCommandFromResourceAssembler.toCommandFromResource(resource));
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/experience/{experienceId}")
    public ResponseEntity<?> deleteExperience(@PathVariable Long experienceId) {
        try {

            driverCommandService.handle(new DeleteDriverExperienceCommand(experienceId));
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    /*
     * =================  DRIVER VEHICLE SERVICES  =================
     */

    @GetMapping("/{driverId}/vehicle/")
    public ResponseEntity<?> getAllVehiclesByDriverId(@PathVariable Long driverId) {
        try {

            List<Vehicle> vehicles = driverQueryService.handle(new GetAllVehiclesByDriverIdQuery(driverId));

            if (vehicles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                var driverVehicleResources = vehicles.stream()
                        .map(DriverVehicleResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverVehicleResources);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/vehicle")
    public ResponseEntity<?> addVehicle(@Valid @RequestBody AddDriverVehicleResource resource) {
        try {

            driverCommandService.handle(AddDriverVehicleCommandFromResourceAssembler.toCommandFromResource(resource));
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/vehicle/{vehicleId}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long vehicleId) {
        try {

            driverCommandService.handle(new DeleteDriverVehicleCommand(vehicleId));
            return ResponseEntity.status(HttpStatus.OK).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    /*
     * =================  DRIVER COMMENT SERVICES  =================
     */

    @GetMapping("/{driverId}/comment/")
    public ResponseEntity<?> getAllCommentsByDriverId(@PathVariable Long driverId) {
        try {

            List<Comment> comments = driverQueryService.handle(new GetAllCommentsByDriverIdQuery(driverId));

            if (comments.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                var driverCommentResources = comments.stream()
                        .map(DriverCommentResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverCommentResources);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    /*
     * =================  DRIVER RATING SERVICES  =================
     */

    @GetMapping(value = "/most-ranked", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getMostRankedDrivers() {
        try {

            List<Driver> drivers = driverQueryService.handle(new GetMostRankedDriversQuery());

            if (drivers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                var driverSummaryResources = drivers.stream()
                        .map(DriverSummaryResourceFromEntityAssembler::assemble)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverSummaryResources);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

}
