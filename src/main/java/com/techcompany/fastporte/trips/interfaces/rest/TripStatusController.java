package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.shared.response.ErrorResponse;
import com.techcompany.fastporte.shared.response.SuccessResponse;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.TripStatus;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllTripStatusQuery;
import com.techcompany.fastporte.trips.domain.services.TripStatusQueryService;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity.TripStatusResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/trip-status")
@Tag(name = "Trip Status Management", description = "Operations for retrieving trip statuses")
public class TripStatusController {

    private final TripStatusQueryService tripStatusQueryService;

    public TripStatusController(TripStatusQueryService tripStatusQueryService) {
        this.tripStatusQueryService = tripStatusQueryService;
    }

    @Operation(summary = "Get all trip statuses", description = "Retrieves a list of all possible statuses for trips.")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTripStatus() {
        try{
            List<TripStatus> tripStatusList = tripStatusQueryService.handle(new GetAllTripStatusQuery());

            if (tripStatusList.isEmpty()) {

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("Estados de viaje no encontrados"));

            } else {

                var tripStatusResourceList = tripStatusList.stream()
                        .map(TripStatusResourceFromEntityAssembler::fromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(tripStatusResourceList);
            }

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }


    }
}
