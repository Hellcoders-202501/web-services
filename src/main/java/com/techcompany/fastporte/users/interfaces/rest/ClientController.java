package com.techcompany.fastporte.users.interfaces.rest;

import com.techcompany.fastporte.shared.response.ErrorResponse;
import com.techcompany.fastporte.shared.response.SuccessResponse;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Client;
import com.techcompany.fastporte.users.domain.model.commands.client.DeleteClientCommand;
import com.techcompany.fastporte.users.domain.model.queries.client.GetAllClientsQuery;
import com.techcompany.fastporte.users.domain.model.queries.client.GetClientByIdQuery;
import com.techcompany.fastporte.users.domain.services.client.ClientCommandService;
import com.techcompany.fastporte.users.domain.services.client.ClientQueryService;
import com.techcompany.fastporte.users.interfaces.rest.resources.RegisterClientResource;
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

    public ClientController(ClientCommandService clientCommandService, ClientQueryService clientQueryService) {
        this.clientCommandService = clientCommandService;
        this.clientQueryService = clientQueryService;
    }

    @Operation(summary = "Get a client by ID", description = "Retrieves the details of a specific client by their ID.")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long id) {

        try {
            Optional<Client> client = clientQueryService.handle(new GetClientByIdQuery(id));

            if (client.isPresent()) {
                var clientR = ClientInformationResourceFromEntityAssembler.toResourceFromEntity(client.get());
                return ResponseEntity.status(HttpStatus.OK).body(clientR);

            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Cliente no encontrado"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Get all clients", description = "Retrieves a list of all clients.")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findAll() {
        try {
            List<Client> clients = clientQueryService.handle(new GetAllClientsQuery());

            if (clients.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new SuccessResponse("No se encontraron clientes"));
            } else {
                var clientInformationResources = clients.stream()
                        .map(ClientInformationResourceFromEntityAssembler::toResourceFromEntity)
                        .toList();

                return ResponseEntity.status(HttpStatus.OK).body(clientInformationResources);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @Operation(summary = "Create a new client", description = "Creates a new client with the provided details.")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> save(@Valid @RequestBody RegisterClientResource resource) {

        try {

            Optional<Client> client = clientCommandService.handle(RegisterClientCommandFromResourceAssembler.toCommandFromResource(resource));

            if (client.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Cliente registrado con éxito"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("No se pudo registrar al cliente. Consultar con Soporte."));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }

    }

    @Operation(summary = "Update client information", description = "Updates the details of a client.")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody UpdateClientInformationResource resource) {

        try {

            Optional<Client> client = clientCommandService.handle(UpdateClientInformationCommandFromResourceAssembler.toCommandFromResource(resource));

            if (client.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Información del cliente actualizada"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("La información del cliente no se pudo actualizar. Consultar con Soporte."));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }

    }

    @Operation(summary = "Delete a client by ID", description = "Deletes the client with the specified ID.")
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_DRIVER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            if (clientQueryService.handle(new GetClientByIdQuery(id)).isPresent()) {
                clientCommandService.handle(new DeleteClientCommand(id));

                return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Cliente eliminado"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Cliente a eliminar no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

}