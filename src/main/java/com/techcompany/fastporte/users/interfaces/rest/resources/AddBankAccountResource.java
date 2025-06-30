package com.techcompany.fastporte.users.interfaces.rest.resources;

public record AddBankAccountResource(
        Long driverId,
        String bankName,
        String accountNumber,
        Long accountTypeId
) {
}
