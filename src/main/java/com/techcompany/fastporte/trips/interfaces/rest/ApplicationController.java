package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.shared.response.ErrorResponse;
import com.techcompany.fastporte.shared.response.SuccessResponse;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Application;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteApplicationCommand;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllApplicationsByRequestIdQuery;
import com.techcompany.fastporte.trips.domain.services.ApplicationCommandService;
import com.techcompany.fastporte.trips.domain.services.ApplicationQueryService;
import com.techcompany.fastporte.trips.interfaces.rest.resources.ApplyToRequestResource;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity.ApplicationResourceFromEntityAssembler;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromResource.ApplyToRequestCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/applications")
@Tag(name = "Application Management", description = "Operations for managing applications including retrieval")
public class ApplicationController {

    private final ApplicationCommandService applicationCommandService;
    private final ApplicationQueryService applicationQueryService;

    public ApplicationController(ApplicationCommandService applicationCommandService, ApplicationQueryService applicationQueryService) {
        this.applicationCommandService = applicationCommandService;
        this.applicationQueryService = applicationQueryService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ApplyToRequest(@Valid @RequestBody ApplyToRequestResource resource) {
        try {

            applicationCommandService.handle(ApplyToRequestCommandFromResourceAssembler.toCommandFromResource(resource));
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Se aplicó a la solicitud exitosamente"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @GetMapping(path = "/request/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> GetApplicationsByRequestId(@PathVariable("requestId") Long requestId) {
        try {

            List<Application> applications = applicationQueryService.handle(new GetAllApplicationsByRequestIdQuery(requestId));

            if (applications.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Sin registros"));
            }else {

                var applicationResources = ApplicationResourceFromEntityAssembler.toResourceFromEntity(applications);
                return ResponseEntity.status(HttpStatus.OK).body(applicationResources);
            }


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> DeleteApplication(@PathVariable("id") Long id) {
        try {

            applicationCommandService.handle(new DeleteApplicationCommand(id));
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Aplicación eliminada"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

}

