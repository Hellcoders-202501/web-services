package com.techcompany.fastporte.users.interfaces.rest;

import com.techcompany.fastporte.shared.exception.ErrorResponse;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.commands.client.DeleteClientCommand;
import com.techcompany.fastporte.users.domain.model.queries.client.GetAllClientsQuery;
import com.techcompany.fastporte.users.domain.model.queries.client.GetClientByIdQuery;
import com.techcompany.fastporte.users.domain.services.driver.DriverQueryService;
import com.techcompany.fastporte.users.domain.services.client.ClientCommandService;
import com.techcompany.fastporte.users.domain.services.client.ClientQueryService;
import com.techcompany.fastporte.users.interfaces.rest.resources.RegisterClientResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.ClientInformationResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.UpdateClientInformationResource;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity.ClientInformationResourceFromEntityAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromResource.RegisterClientCommandFromResourceAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromResource.UpdateClientInformationCommandFromResourceAssembler;
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
@RequestMapping("/api/v1/clients")
@Tag(name = "Client Management", description = "Operations for managing clients, including creation, updating, and retrieval")
public class ClientController {

    private final ClientCommandService clientCommandService;
    private final ClientQueryService clientQueryService;
    private final DriverQueryService driverQueryService;

    public ClientController(ClientCommandService clientCommandService, ClientQueryService clientQueryService, DriverQueryService driverQueryService) {
        this.clientCommandService = clientCommandService;
        this.clientQueryService = clientQueryService;
        this.driverQueryService = driverQueryService;
    }

    @Operation(summary = "Get a client by ID", description = "Retrieves the details of a specific client by their ID.")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {

        try {
            Optional<Client> client = clientQueryService.handle(new GetClientByIdQuery(id));

            return client.map(value -> ResponseEntity.ok(ClientInformationResourceFromEntityAssembler.toResourceFromEntity(value)))
                    .orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients.")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        try {
            List<Client> clients = clientQueryService.handle(new GetAllClientsQuery());

            if (clients.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                var clientInformationResources = clients.stream()
                        .map(ClientInformationResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(clientInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Create a new client", description = "Creates a new client with the provided details.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientInformationResource> save(@Valid @RequestBody RegisterClientResource resource) {

        Optional<Client> client = clientCommandService.handle(RegisterClientCommandFromResourceAssembler.toCommandFromResource(resource));
        return client.map(ClientInformationResourceFromEntityAssembler::toResourceFromEntity)
                .map(clientInformationResource -> ResponseEntity.status(HttpStatus.CREATED).body(clientInformationResource))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
    }

    @Operation(summary = "Update client information", description = "Updates the details of a client.")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientInformationResource> update(@RequestBody UpdateClientInformationResource resource) {

        Optional<Client> client = clientCommandService.handle(UpdateClientInformationCommandFromResourceAssembler.toCommandFromResource(resource));
        return client.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(ClientInformationResourceFromEntityAssembler.toResourceFromEntity(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));

    }

    @Operation(summary = "Delete a client by ID", description = "Deletes the client with the specified ID.")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (clientQueryService.handle(new GetClientByIdQuery(id)).isPresent()) {
                clientCommandService.handle(new DeleteClientCommand(id));

                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage()));
        }
    }

    /*
    @Operation(summary = "Get drivers by client", description = "Retrieves all drivers managed by the specified client.")
    @GetMapping(value = "/{clientId}/drivers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DriverInformationResource>> findAllBySupervisorId(@PathVariable Long clientId) {
        try{
            List<Driver> drivers = driverQueryService.handle(new GetAllDriversBySupervisorIdQuery(clientId));

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
    */
}