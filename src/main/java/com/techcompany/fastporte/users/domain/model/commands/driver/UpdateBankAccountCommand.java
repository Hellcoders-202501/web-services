package com.techcompany.fastporte.users.domain.model.commands.driver;

public record UpdateBankAccountCommand(
        Long id,
        String bankName,
        String accountNumber,
        Long accountTypeId
) {
}
