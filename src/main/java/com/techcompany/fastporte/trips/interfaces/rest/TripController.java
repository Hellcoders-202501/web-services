package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Trip;
import com.techcompany.fastporte.trips.domain.model.commands.CancelTripCommand;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteTripCommand;
import com.techcompany.fastporte.trips.domain.model.commands.FinishTripCommand;
import com.techcompany.fastporte.trips.domain.model.commands.StartTripCommand;
import com.techcompany.fastporte.trips.domain.model.queries.*;
import com.techcompany.fastporte.trips.domain.services.TripCommandService;
import com.techcompany.fastporte.trips.domain.services.TripQueryService;
import com.techcompany.fastporte.trips.interfaces.rest.resources.CreateTripResource;
import com.techcompany.fastporte.trips.interfaces.rest.resources.TripInformationResource;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity.TripInformationResourceFromEntityAssembler;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromResource.CreateTripCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/trips")
@PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
@Tag(name = "Trip Management", description = "Operations related to managing trips")
public class TripController {

    private final TripCommandService tripCommandService;
    private final TripQueryService tripQueryService;

    public TripController(TripCommandService tripCommandService, TripQueryService tripQueryService) {
        this.tripCommandService = tripCommandService;
        this.tripQueryService = tripQueryService;
    }

    @Operation(summary = "Get a trip by ID", description = "Retrieves the details of a specific trip by its ID.")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TripInformationResource> findById(@PathVariable Long id) {
        try {
            Optional<Trip> trip = tripQueryService.handle(new GetTripByIdQuery(id));

            return trip.map(value -> ResponseEntity.status(HttpStatus.OK).body(TripInformationResourceFromEntityAssembler.toResourceFromEntity(value)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get all trips", description = "Retrieves a list of all trips.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripInformationResource>> findAll() {
        try {
            List<Trip> trips = tripQueryService.handle(new GetAllTripsQuery());

            if (trips.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                var tripInformationResources = trips.stream().map(TripInformationResourceFromEntityAssembler::toResourceFromEntity).toList();

                return ResponseEntity.status(HttpStatus.OK).body(tripInformationResources);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get trips by client", description = "Retrieves all trips managed by the specified client.")
    @GetMapping(value = "/client/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripInformationResource>> findByClientId(@PathVariable Long clientId) {
        try {
            List<Trip> trips = tripQueryService.handle(new GetTripsByClientIdQuery(clientId));

            if (trips.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                var tripInformationResources = trips.stream().map(TripInformationResourceFromEntityAssembler::toResourceFromEntity).toList();

                return ResponseEntity.status(HttpStatus.OK).body(tripInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get trips by driver", description = "Retrieves all trips assigned to the specified driver.")
    @GetMapping(value = "/driver/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripInformationResource>> findByDriverId(@PathVariable Long driverId) {
        try {
            List<Trip> trips = tripQueryService.handle(new GetTripsByDriverIdQuery(driverId));

            if (trips.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                var tripInformationResources = trips.stream().map(TripInformationResourceFromEntityAssembler::toResourceFromEntity).toList();

                return ResponseEntity.status(HttpStatus.OK).body(tripInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get trips by driver and status", description = "Retrieves trips for a driver filtered by the specified status.")
    @GetMapping(value = "/driver/{driverId}/status/{statusId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripInformationResource>> findByDriverIdAndStatus(@PathVariable Long driverId, @PathVariable Long statusId) {
        try {
            List<Trip> trips = tripQueryService.handle(new GetTripsByDriverIdAndStatusQuery(driverId, statusId));

            if (trips.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                var tripInformationResources = trips.stream().map(TripInformationResourceFromEntityAssembler::toResourceFromEntity).toList();

                return ResponseEntity.status(HttpStatus.OK).body(tripInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get trips by client and status", description = "Retrieves trips for a client filtered by the specified status.")
    @GetMapping(value = "/client/{clientId}/status/{statusId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripInformationResource>> findByClientIdAndStatus(@PathVariable Long clientId, @PathVariable Long statusId) {
        try {
            List<Trip> trips = tripQueryService.handle(new GetTripsByClientIdAndStatusQuery(clientId, statusId));

            if (trips.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                var tripInformationResources = trips.stream().map(TripInformationResourceFromEntityAssembler::toResourceFromEntity).toList();

                return ResponseEntity.status(HttpStatus.OK).body(tripInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Create a new trip", description = "Creates a new trip with the specified details.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TripInformationResource> save(@Valid @RequestBody CreateTripResource resource) {
        try {
            Optional<Trip> trip = tripCommandService.handle(CreateTripCommandFromResourceAssembler.toCommandFromResource(resource));
            return trip.map(TripInformationResourceFromEntityAssembler::toResourceFromEntity).map(tripInformationResource -> ResponseEntity.status(HttpStatus.CREATED).body(tripInformationResource)).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Delete a trip by ID", description = "Deletes the trip with the given ID.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            if (tripQueryService.handle(new CheckTripExistsByIdQuery(id))) {
                tripCommandService.handle(new DeleteTripCommand(id));
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Start a trip", description = "Marks the trip with the given ID as started.")
    @PostMapping(value = "/{id}/starts")
    public ResponseEntity<Void> start(@PathVariable Long id) {
        try {
            if (tripQueryService.handle(new CheckTripExistsByIdQuery(id))) {
                tripCommandService.handle(new StartTripCommand(id));
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Complete a trip", description = "Marks the trip with the given ID as completed.")
    @PostMapping(value = "/{id}/completions")
    public ResponseEntity<Void> finish(@PathVariable Long id) {
        try {
            if (tripQueryService.handle(new CheckTripExistsByIdQuery(id))) {
                tripCommandService.handle(new FinishTripCommand(id));
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Cancel a trip", description = "Cancels the trip with the given ID.")
    @PostMapping(value = "/{id}/cancellations")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        try {
            if (tripQueryService.handle(new CheckTripExistsByIdQuery(id))) {
                tripCommandService.handle(new CancelTripCommand(id));
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
