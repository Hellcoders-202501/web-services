package com.techcompany.fastporte.users.interfaces.rest;

import com.techcompany.fastporte.shared.response.ErrorResponse;
import com.techcompany.fastporte.shared.response.SuccessResponse;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.BankAccount;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.BankAccountType;
import com.techcompany.fastporte.users.domain.model.commands.driver.AddBankAccountCommand;
import com.techcompany.fastporte.users.domain.model.commands.driver.DeleteBankAccountCommand;
import com.techcompany.fastporte.users.domain.model.commands.driver.UpdateBankAccountCommand;
import com.techcompany.fastporte.users.domain.model.queries.driver.GetAllBankAccountTypesQuery;
import com.techcompany.fastporte.users.domain.model.queries.driver.GetBankAccountQuery;
import com.techcompany.fastporte.users.domain.services.driver.DriverCommandService;
import com.techcompany.fastporte.users.domain.services.driver.DriverQueryService;
import com.techcompany.fastporte.users.interfaces.rest.resources.AddBankAccountResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.BankAccountResource;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity.BankAccountResourceFromEntityAssembler;
import com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity.DriverInformationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/bank-account")
@Tag(name = "Bank Account Management", description = "Operations for managing Bank Accounts, including creation, updating, and retrieval")

public class BankAccountController {

    private final DriverCommandService driverCommandService;
    private final DriverQueryService driverQueryService;

    public BankAccountController(DriverCommandService driverCommandService, DriverQueryService driverQueryService) {
        this.driverCommandService = driverCommandService;
        this.driverQueryService = driverQueryService;
    }

    @GetMapping(value = "/{driverId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findById(@PathVariable Long driverId) {
        try{

            Optional<BankAccount> bankAccount = driverQueryService.handle(new GetBankAccountQuery(driverId));

            if (bankAccount.isPresent()) {
                var bAccount = BankAccountResourceFromEntityAssembler.toResourceFromEntity(bankAccount.get());
                return ResponseEntity.status(HttpStatus.OK).body(bAccount);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Cuenta bancaria no encontrada"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody AddBankAccountResource resource) {

        try {

            driverCommandService.handle(new AddBankAccountCommand(resource.driverId(), resource.bankName(), resource.accountNumber(), resource.accountTypeId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Cuenta bancaria agregada con exito"));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {

            driverCommandService.handle(new DeleteBankAccountCommand(id));
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Cuenta bancaria eliminada"));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody UpdateBankAccountCommand resource) {
        try {

            driverCommandService.handle(new UpdateBankAccountCommand(resource.id(), resource.bankName(), resource.accountNumber(), resource.accountTypeId()));
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Cuenta bancaria actualizada con exito"));

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }

    @GetMapping(value = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTypes() {
        try{

            List<BankAccountType> bankAccountTypeList = driverQueryService.handle(new GetAllBankAccountTypesQuery());

            if (!bankAccountTypeList.isEmpty()) {
                var bAccount = BankAccountResourceFromEntityAssembler.toResourceFromEntity(bankAccountTypeList);
                return ResponseEntity.status(HttpStatus.OK).body(bAccount);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new SuccessResponse("Tipos de cuenta bancarias no encontradas"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e));
        }
    }
    
}
