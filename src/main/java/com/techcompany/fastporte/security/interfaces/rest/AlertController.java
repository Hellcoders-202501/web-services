package com.techcompany.fastporte.security.interfaces.rest;

import com.techcompany.fastporte.security.domain.model.aggregates.entities.Alert;
import com.techcompany.fastporte.security.domain.model.commands.CreateAlertCommand;
import com.techcompany.fastporte.security.domain.model.queries.GetAlertsByTripIdQuery;
import com.techcompany.fastporte.security.domain.services.AlertCommandService;
import com.techcompany.fastporte.security.domain.services.AlertQueryService;
import com.techcompany.fastporte.security.interfaces.rest.resources.AlertInformationResource;
import com.techcompany.fastporte.security.interfaces.rest.resources.CreateAlertResource;
import com.techcompany.fastporte.security.interfaces.rest.transform.fromEntity.AlertInformationResourceFromEntityAssembler;
import com.techcompany.fastporte.security.interfaces.rest.transform.fromResource.CreateAlertCommandFromResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertCommandService alertCommandService;
    private final AlertQueryService alertQueryService;

    public AlertController(AlertCommandService alertCommandService, AlertQueryService alertQueryService) {
        this.alertCommandService = alertCommandService;
        this.alertQueryService = alertQueryService;
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<AlertInformationResource>> getAlertsByTripId(@PathVariable Long tripId) {
        try {
            List<Alert> alerts = alertQueryService.handle(new GetAlertsByTripIdQuery(tripId));

            if (alerts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {

                var alertsResource = alerts.stream()
                        .map(AlertInformationResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(alertsResource);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AlertInformationResource> createAlert(@RequestBody CreateAlertResource createAlertResource) {
        try {

            CreateAlertCommand createAlertCommand = CreateAlertCommandFromResourceAssembler.fromResource(createAlertResource);
            Optional<Alert> alert = alertCommandService.handle(createAlertCommand);

            if (alert.isEmpty()) {
                System.out.println("Alert not created");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            } else {
                AlertInformationResource alertInformationResource = AlertInformationResourceFromEntityAssembler.toResourceFromEntity(alert.get());
                return ResponseEntity.status(HttpStatus.CREATED).body(alertInformationResource);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
