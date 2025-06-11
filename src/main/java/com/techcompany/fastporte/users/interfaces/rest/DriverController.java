package com.techcompany.fastporte.users.interfaces.rest;

import com.techcompany.fastporte.shared.response.ErrorResponse;
import com.techcompany.fastporte.shared.response.SuccessResponse;
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
                var driverR = DriverInformationResourceFromEntityAssembler.toPublicResourceFromEntity(driver.get());
                return ResponseEntity.status(HttpStatus.OK).body(driverR);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Cliente no encontrado"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Get all drivers", description = "Retrieves a list of all drivers.")
    @PreAuthorize("hasRole('ROLE_DRIVER') or hasRole('ROLE_CLIENT') or hasRole('ROLE_ADMIN') ")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        try{
            List<Driver> drivers = driverQueryService.handle(new GetAllDriversQuery());

            if (drivers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("No se encontraron conductores"));
            } else {

                var driverInformationResources = drivers.stream()
                        .map(DriverInformationResourceFromEntityAssembler::toPublicResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/batch")
    @Operation(summary = "Get drivers in batch", description = "Retrieves a list of drivers by their IDs.")
    public ResponseEntity<?> findAllByIdIn(@RequestBody List<Long> driverIds) {

        try {
            List<Driver> drivers = driverQueryService.handle(new GetAllDriversByIdInList(driverIds));

            if (drivers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("No se encontraron conductores"));
            }

            var driverInformationResources = drivers.stream()
                    .map(DriverInformationResourceFromEntityAssembler::toPublicResourceFromEntity)
                    .toList();

            return ResponseEntity.status(HttpStatus.OK).body(driverInformationResources);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Create a new driver", description = "Creates a new driver with the provided details.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody RegisterDriverResource resource) {

        try {
            Optional<Driver> driver = driverCommandService.handle(RegisterDriverCommandFromResourceAssembler.toCommandFromResource(resource));

            if (driver.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Conductor registrado con éxito"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("No se pudo registrar al conductor. Consultar con Soporte."));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Update driver information", description = "Updates the details of a driver.")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody UpdateDriverInformationResource resource) {

        try {
            Optional<Driver> driver = driverCommandService.handle(UpdateDriverInformationCommandFromResourceAssembler.toCommandFromResource(resource));

            if (driver.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Información del conductor actualizada"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("La información del conductor no se pudo actualizar. Consultar con Soporte."));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Delete a driver by ID", description = "Deletes the driver with the specified ID.")
    @PreAuthorize("hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (driverQueryService.handle(new GetDriverByIdQuery(id)).isPresent()) {
                driverCommandService.handle(new DeleteDriverCommand(id));

                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Conductor eliminado"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Conductor a eliminar no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
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
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("Sin experiencias registradas"));
            } else {
                var driverExperienceResources = experiences.stream()
                        .map(DriverExperienceResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverExperienceResources);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @PostMapping("/experience")
    public ResponseEntity<?> addExperience(@Valid @RequestBody AddDriverExperienceResource resource) {
        try {

            driverCommandService.handle(AddDriverExperienceCommandFromResourceAssembler.toCommandFromResource(resource));
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Experiencia agregada"));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @DeleteMapping("/experience/{experienceId}")
    public ResponseEntity<?> deleteExperience(@PathVariable Long experienceId) {
        try {

            driverCommandService.handle(new DeleteDriverExperienceCommand(experienceId));
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Experiencia eliminada"));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
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
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("Sin vehiculos registrados"));
            } else {
                var driverVehicleResources = vehicles.stream()
                        .map(DriverVehicleResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverVehicleResources);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @PostMapping("/vehicle")
    public ResponseEntity<?> addVehicle(@Valid @RequestBody AddDriverVehicleResource resource) {
        try {

            driverCommandService.handle(AddDriverVehicleCommandFromResourceAssembler.toCommandFromResource(resource));
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Vehiculo agregado"));

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @DeleteMapping("/vehicle/{vehicleId}")
    public ResponseEntity<?> deleteVehicle(@PathVariable Long vehicleId) {
        try {

            driverCommandService.handle(new DeleteDriverVehicleCommand(vehicleId));
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Vehiculo eliminado"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
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
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("Sin commentarios registrados"));
            } else {
                var driverCommentResources = comments.stream()
                        .map(DriverCommentResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverCommentResources);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
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
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("Sin registros"));
            } else {
                var driverSummaryResources = drivers.stream()
                        .map(DriverSummaryResourceFromEntityAssembler::assemble)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverSummaryResources);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

}
