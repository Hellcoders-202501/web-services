package com.techcompany.fastporte.users.interfaces.rest.resources;

import java.util.List;

public record BankAccountResource(
        Long id,
        String bankName,
        String number,
        String type,
        String currency,
        String createdAt,
        List<TransactionResource> transactions
) {
}
