package com.techcompany.fastporte.users.interfaces.rest.resources;

public record BankAccountResource(
        Long id,
        String bankName,
        String number,
        String type,
        String currency,
        String createdAt
) {
}
