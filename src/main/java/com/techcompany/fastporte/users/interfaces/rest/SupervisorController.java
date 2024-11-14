package com.techcompany.fastporte.users.interfaces.rest;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.Driver;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Supervisor;
import com.techcompany.fastporte.users.domain.model.commands.supervisor.DeleteSupervisorCommand;
import com.techcompany.fastporte.users.domain.model.queries.driver.GetAllDriversBySupervisorIdQuery;
import com.techcompany.fastporte.users.domain.model.queries.supervisor.GetAllSupervisorsQuery;
import com.techcompany.fastporte.users.domain.model.queries.supervisor.GetSupervisorByIdQuery;
import com.techcompany.fastporte.users.domain.services.driver.DriverQueryService;
import com.techcompany.fastporte.users.domain.services.supervisor.SupervisorCommandService;
import com.techcompany.fastporte.users.domain.services.supervisor.SupervisorQueryService;
import com.techcompany.fastporte.users.interfaces.rest.resources.DriverInformationResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.RegisterSupervisorResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.SupervisorInformationResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.UpdateSupervisorInformationResource;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity.DriverInformationResourceFromEntityAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity.SupervisorInformationResourceFromEntityAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromResource.RegisterSupervisorCommandFromResourceAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromResource.UpdateSupervisorInformationCommandFromResourceAssembler;
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
@RequestMapping("/api/v1/supervisors")
@Tag(name = "Supervisor Management", description = "Operations for managing supervisors, including creation, updating, and retrieval")
public class SupervisorController {

    private final SupervisorCommandService supervisorCommandService;
    private final SupervisorQueryService supervisorQueryService;
    private final DriverQueryService driverQueryService;

    public SupervisorController(SupervisorCommandService supervisorCommandService, SupervisorQueryService supervisorQueryService, DriverQueryService driverQueryService) {
        this.supervisorCommandService = supervisorCommandService;
        this.supervisorQueryService = supervisorQueryService;
        this.driverQueryService = driverQueryService;
    }

    @Operation(summary = "Get a supervisor by ID", description = "Retrieves the details of a specific supervisor by their ID.")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupervisorInformationResource> findById(@PathVariable Long id) {

        try {
            Optional<Supervisor> supervisor = supervisorQueryService.handle(new GetSupervisorByIdQuery(id));

            return supervisor.map(value -> ResponseEntity.ok(SupervisorInformationResourceFromEntityAssembler.toResourceFromEntity(value)))
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get all supervisors", description = "Retrieves a list of all supervisors.")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SupervisorInformationResource>> findAll() {
        try {
            List<Supervisor> supervisors = supervisorQueryService.handle(new GetAllSupervisorsQuery());

            if (supervisors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                var supervisorInformationResources = supervisors.stream()
                        .map(SupervisorInformationResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(supervisorInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Create a new supervisor", description = "Creates a new supervisor with the provided details.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupervisorInformationResource> save(@Valid @RequestBody RegisterSupervisorResource resource) {

        Optional<Supervisor> supervisor = supervisorCommandService.handle(RegisterSupervisorCommandFromResourceAssembler.toCommandFromResource(resource));
        return supervisor.map(SupervisorInformationResourceFromEntityAssembler::toResourceFromEntity)
                .map(supervisorInformationResource -> ResponseEntity.status(HttpStatus.CREATED).body(supervisorInformationResource))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
    }

    @Operation(summary = "Update supervisor information", description = "Updates the details of a supervisor.")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SupervisorInformationResource> update(@RequestBody UpdateSupervisorInformationResource resource) {

        Optional<Supervisor> supervisor = supervisorCommandService.handle(UpdateSupervisorInformationCommandFromResourceAssembler.toCommandFromResource(resource));
        return supervisor.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(SupervisorInformationResourceFromEntityAssembler.toResourceFromEntity(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));

    }

    @Operation(summary = "Delete a supervisor by ID", description = "Deletes the supervisor with the specified ID.")
    @PreAuthorize("hasRole('ROLE_SUPERVISOR') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            if (supervisorQueryService.handle(new GetSupervisorByIdQuery(id)).isPresent()) {
                supervisorCommandService.handle(new DeleteSupervisorCommand(id));

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get drivers by supervisor", description = "Retrieves all drivers managed by the specified supervisor.")
    @GetMapping(value = "/{supervisorId}/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DriverInformationResource>> findAllBySupervisorId(@PathVariable Long supervisorId) {
        try{
            List<Driver> drivers = driverQueryService.handle(new GetAllDriversBySupervisorIdQuery(supervisorId));

            if (drivers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                var driverInformationResources = drivers.stream()
                        .map(DriverInformationResourceFromEntityAssembler::toPublicResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(driverInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}