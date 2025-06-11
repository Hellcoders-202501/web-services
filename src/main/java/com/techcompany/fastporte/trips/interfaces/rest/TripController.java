package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.shared.response.ErrorResponse;
import com.techcompany.fastporte.shared.response.SuccessResponse;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.TripStatus;
import com.techcompany.fastporte.trips.domain.model.commands.*;
import com.techcompany.fastporte.trips.domain.model.queries.*;
import com.techcompany.fastporte.trips.domain.services.TripCommandService;
import com.techcompany.fastporte.trips.domain.services.TripQueryService;
import com.techcompany.fastporte.trips.domain.services.TripStatusQueryService;
import com.techcompany.fastporte.trips.interfaces.rest.resources.AddCommentResource;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity.RequestResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/trips")
@PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
@Tag(name = "Trip Management", description = "Operations related to managing trips")
public class TripController {

    private final TripCommandService tripCommandService;
    private final TripQueryService tripQueryService;
    private final TripStatusQueryService tripStatusQueryService;

    public TripController(TripCommandService tripCommandService, TripQueryService tripQueryService, TripStatusQueryService tripStatusQueryService) {
        this.tripCommandService = tripCommandService;
        this.tripQueryService = tripQueryService;
        this.tripStatusQueryService = tripStatusQueryService;
    }

    @Operation(summary = "Get a trip by ID", description = "Retrieves the details of a specific trip by its ID.")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Optional<Request> trip = tripQueryService.handle(new GetTripByIdQuery(id));

            if (trip.isPresent()) {
                var tripR = RequestResourceFromEntityAssembler.toResourceFromEntity(trip.get());
                return ResponseEntity.status(HttpStatus.OK).body(tripR);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Viaje no encontrado"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Get all trips", description = "Retrieves a list of all trips.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        try {
            List<Request> trips = tripQueryService.handle(new GetAllTripsQuery());

            if (trips.isEmpty()) {

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("No se encontraron viajes registrados"));

            } else {

                var requestResources = trips.stream().map(RequestResourceFromEntityAssembler::toResourceFromEntity).toList();
                return ResponseEntity.status(HttpStatus.OK).body(requestResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Get trips by client", description = "Retrieves all trips managed by the specified client.")
    @GetMapping(value = "/client/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByClientId(@PathVariable Long clientId) {
        try {
            List<Request> trips = tripQueryService.handle(new GetTripsByClientIdQuery(clientId));

            if (trips.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("No se encontraron viajes para el cliente"));
            } else {

                var requestResources = trips.stream().map(RequestResourceFromEntityAssembler::toResourceFromEntity).toList();
                return ResponseEntity.status(HttpStatus.OK).body(requestResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Get trips by driver", description = "Retrieves all trips assigned to the specified driver.")
    @GetMapping(value = "/driver/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByDriverId(@PathVariable Long driverId) {
        try {
            List<Request> trips = tripQueryService.handle(new GetTripsByDriverIdQuery(driverId));

            if (trips.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("No se encontraron viajes para el conductor"));
            } else {

                var requestResources = trips.stream().map(RequestResourceFromEntityAssembler::toResourceFromEntity).toList();
                return ResponseEntity.status(HttpStatus.OK).body(requestResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Get trips by driver and status", description = "Retrieves trips for a driver filtered by the specified status.")
    @GetMapping(value = "/driver/{driverId}/status/{statusId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByDriverIdAndStatus(@PathVariable Long driverId, @PathVariable Long statusId) {
        try {
            List<Request> trips = tripQueryService.handle(new GetTripsByDriverIdAndStatusQuery(driverId, statusId));

            if (trips.isEmpty()) {

                String state = getStringStatus(statusId);;
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("No se encontraron viajes con el estado " + state + " para el conductor solicitado"));

            } else {

                var requestResources = trips.stream().map(RequestResourceFromEntityAssembler::toResourceFromEntity).toList();
                return ResponseEntity.status(HttpStatus.OK).body(requestResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    private String getStringStatus(@PathVariable Long statusId) {

        List<TripStatus> tripStatuses = tripStatusQueryService.handle(new GetAllTripStatusQuery());
        TripStatus tS = tripStatuses.stream().filter(t -> t.getId().equals(statusId)).findFirst().orElse(null);
        return switch (Objects.requireNonNull(tS).getStatus()) {
            case PENDING -> "PENDIENTE";
            case STARTED -> "INICIADO";
            case FINISHED_BY_DRIVER -> "FINALIZADO por conductor";
            case FINISHED_BY_CLIENT -> "FINALIZADO por cliente";
            case COMPLETED -> "COMPLETADO";
            default -> "-----";
        };
    }

    @Operation(summary = "Get trips by client and status", description = "Retrieves trips for a client filtered by the specified status.")
    @GetMapping(value = "/client/{clientId}/status/{statusId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findByClientIdAndStatus(@PathVariable Long clientId, @PathVariable Long statusId) {
        try {

            List<Request> trips = tripQueryService.handle(new GetTripsByClientIdAndStatusQuery(clientId, statusId));

            if (trips.isEmpty()) {

                String state = getStringStatus(statusId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("No se encontraron viajes con el estado " + state + " para el cliente solicitado"));

            } else {

                var requestResources = trips.stream().map(RequestResourceFromEntityAssembler::toResourceFromEntity).toList();
                return ResponseEntity.status(HttpStatus.OK).body(requestResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

//    @Operation(summary = "Create a new trip", description = "Creates a new trip with the specified details.")
//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> save(@Valid @RequestBody CreateTripResource resource) {
//        try {
//            Optional<Trip> trip = tripCommandService.handle(CreateTripCommandFromResourceAssembler.toCommandFromResource(resource));
//            return trip.map(TripInformationResourceFromEntityAssembler::toResourceFromEntity)
//                    .map(tripInformationResource -> ResponseEntity.status(HttpStatus.CREATED).body(tripInformationResource))
//                    .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

//    @Operation(summary = "Delete a trip by ID", description = "Deletes the trip with the given ID.")
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<?> delete(@PathVariable Long id) {
//        try {
//            if (tripQueryService.handle(new CheckTripExistsByIdQuery(id))) {
//                tripCommandService.handle(new DeleteTripCommand(id));
//                return ResponseEntity.status(HttpStatus.OK).build();
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
//        }
//    }

    @Operation(summary = "Start a trip", description = "Marks the trip with the given ID as started.")
    @PostMapping(value = "/{id}/starts")
    public ResponseEntity<?> start(@PathVariable Long id) {
        try {
            if (tripQueryService.handle(new CheckTripExistsByIdQuery(id))) {
                tripCommandService.handle(new StartTripCommand(id));
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("El estado del viaje cambió a INICIADO"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("El viaje a iniciar no existe"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Finish a trip by driver", description = "Marks the trip with the given ID as completed by driver.")
    @PostMapping(value = "/{id}/finish-by-driver")
    public ResponseEntity<?> finishByDriver(@PathVariable Long id) {
        try {
            if (tripQueryService.handle(new CheckTripExistsByIdQuery(id))) {
                tripCommandService.handle(new FinishTripByDriverCommand(id));
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("El estado del viaje cambió a FINALIZADO por el conductor"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("El viaje a finalizar no existe"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Finish a trip by client", description = "Marks the trip with the given ID as completed by client.")
    @PostMapping(value = "/{id}/finish-by-client")
    public ResponseEntity<?> finishByClient(@PathVariable Long id) {
        try {
            if (tripQueryService.handle(new CheckTripExistsByIdQuery(id))) {
                tripCommandService.handle(new FinishTripByClientCommand(id));
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("El estado del viaje cambió a FINALIZADO por el cliente"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("El viaje a finalizar no existe"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

//    @Operation(summary = "Cancel a trip", description = "Cancels the trip with the given ID.")
//    @PostMapping(value = "/{id}/cancellations")
//    public ResponseEntity<Void> cancel(@PathVariable Long id) {
//        try {
//            if (tripQueryService.handle(new CheckTripExistsByIdQuery(id))) {
//                tripCommandService.handle(new CancelTripCommand(id));
//                return ResponseEntity.status(HttpStatus.OK).build();
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }


    @PostMapping(path = "/add-comment", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCommentToTrip(@RequestBody AddCommentResource resource) {
        try{

            tripCommandService.handle(new AddCommentCommand(resource.content(), resource.rating(), resource.tripId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Comentario agregado"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }
}
