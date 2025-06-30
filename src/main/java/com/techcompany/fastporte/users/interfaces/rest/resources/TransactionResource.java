package com.techcompany.fastporte.users.interfaces.rest.resources;

import java.time.LocalDateTime;

public record TransactionResource(
        Double amount,
        LocalDateTime transactionDate
) {
}