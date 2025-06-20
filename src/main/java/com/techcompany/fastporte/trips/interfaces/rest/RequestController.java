package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.shared.response.ErrorResponse;
import com.techcompany.fastporte.shared.response.SuccessResponse;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Request;
import com.techcompany.fastporte.trips.domain.model.commands.DeleteRequestCommand;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllRequestsByClientIdAndNotTakenQuery;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllRequestsByServiceIdAndNotTakenQuery;
import com.techcompany.fastporte.trips.domain.model.queries.GetRequestByIdQuery;
import com.techcompany.fastporte.trips.domain.services.RequestCommandService;
import com.techcompany.fastporte.trips.domain.services.RequestQueryService;
import com.techcompany.fastporte.trips.interfaces.rest.resources.PublishRequestResource;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity.RequestResourceFromEntityAssembler;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromResource.PublishRequestCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/request")
@Tag(name = "Request Management", description = "Operations for retrieving requests")
public class RequestController {

    private final RequestQueryService requestQueryService;
    private final RequestCommandService requestCommandService;

    public RequestController(RequestQueryService requestQueryService, RequestCommandService requestCommandService) {
        this.requestQueryService = requestQueryService;
        this.requestCommandService = requestCommandService;
    }

    @GetMapping(path = "/service/{serviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRequestsByServiceIdAndNotTaken(@PathVariable Long serviceId) {
        try{

            List<Request> requests = requestQueryService.handle(new GetAllRequestsByServiceIdAndNotTakenQuery(serviceId));

            if (requests.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Sin registros"));
            } else {

                var requestResources = requests.stream()
                        .map(RequestResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(requestResources);
            }

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }

    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRequestById(@PathVariable Long id) {
        try {
            Optional<Request> request = requestQueryService.handle(new GetRequestByIdQuery(id));

            if (request.isPresent()) {

                var requestR = RequestResourceFromEntityAssembler.toResourceFromEntity(request.get());
                return ResponseEntity.status(HttpStatus.OK).body(requestR);
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Solicitud no encontrada"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @GetMapping(path = "/client/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRequestsByClientId(@PathVariable Long clientId) {

        try {

            List<Request> requests = requestQueryService.handle(new GetAllRequestsByClientIdAndNotTakenQuery(clientId));

            if (requests.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Sin registros"));
            } else {

                var requestResources = requests.stream()
                        .map(RequestResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(requestResources);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> publishRequest(@Valid @RequestBody PublishRequestResource resource) {
        try {

            Optional<Request> request = requestCommandService.handle(PublishRequestCommandFromResourceAssembler.toCommandFromResource(resource));

            if (request.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Solicitud publicada exitosamente"));
            }
             else {
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("No se pudo publicar la solicitud. Consultar con Soporte."));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @DeleteMapping(path = "/{requestId}")
    public ResponseEntity<?> deleteRequest(@PathVariable Long requestId) {

        try {

            requestCommandService.handle(new DeleteRequestCommand(requestId));
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Solicitud eliminada"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }
}