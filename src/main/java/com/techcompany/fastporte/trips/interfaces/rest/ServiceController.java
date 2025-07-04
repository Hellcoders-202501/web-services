package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.shared.response.ErrorResponse;
import com.techcompany.fastporte.shared.response.SuccessResponse;
import com.techcompany.fastporte.trips.domain.model.aggregates.entities.Service;
import com.techcompany.fastporte.trips.domain.model.queries.GetAllServiceTypesQuery;
import com.techcompany.fastporte.trips.domain.services.ServiceQueryService;
import com.techcompany.fastporte.trips.interfaces.rest.transform.fromEntity.ServiceResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/service-type")
@Tag(name = "Type of Services Management", description = "Operations for retrieving type of services")
public class ServiceController {

    private final ServiceQueryService serviceQueryService;

    public ServiceController(ServiceQueryService serviceQueryService) {
        this.serviceQueryService = serviceQueryService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllTypeOfServices() {
        try {

            List<Service> services = serviceQueryService.handle(new GetAllServiceTypesQuery());

            if (services.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Tipos de servicio no encontrados"));
            } else {
                var serviceResourceList = services.stream()
                        .map(ServiceResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(serviceResourceList);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }
}
