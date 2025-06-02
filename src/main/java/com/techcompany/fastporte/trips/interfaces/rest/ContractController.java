package com.techcompany.fastporte.trips.interfaces.rest;

import com.techcompany.fastporte.trips.domain.model.commands.AcceptDriverApplicationCommand;
import com.techcompany.fastporte.trips.domain.services.ContractCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/contracts")
@Tag(name = "Contract Management", description = "Operations for managing contract including retrieval")
public class ContractController {

    private final ContractCommandService contractCommandService;

    public ContractController(ContractCommandService contractCommandService) {
        this.contractCommandService = contractCommandService;
    }

    @PostMapping(path = "/{applicationId}/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createContract(@PathVariable Long applicationId) {
        try {

            contractCommandService.handle(new AcceptDriverApplicationCommand(applicationId));
            return ResponseEntity.status(HttpStatus.CREATED).build();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
