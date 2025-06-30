package com.techcompany.fastporte.users.domain.model.commands.driver;

public record AddBankAccountCommand(
        Long driverId,
        String bankName,
        String accountNumber,
        Long accountTypeId
) {
}
