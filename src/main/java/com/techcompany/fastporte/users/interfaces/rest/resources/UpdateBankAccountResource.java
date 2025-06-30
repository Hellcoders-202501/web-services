package com.techcompany.fastporte.users.interfaces.rest.resources;

public record UpdateBankAccountResource(
        Long id,
        String bankName,
        String accountNumber,
        Long accountTypeId
) {
}
