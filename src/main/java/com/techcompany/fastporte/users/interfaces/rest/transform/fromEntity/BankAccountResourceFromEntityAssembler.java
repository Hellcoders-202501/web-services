package com.techcompany.fastporte.users.interfaces.rest.transform.fromEntity;

import com.techcompany.fastporte.users.domain.model.aggregates.entities.BankAccount;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.BankAccountType;
import com.techcompany.fastporte.users.domain.model.aggregates.entities.Transaction;
import com.techcompany.fastporte.users.interfaces.rest.resources.BankAccountResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.BankAccountTypeResource;
import com.techcompany.fastporte.users.interfaces.rest.resources.TransactionResource;

import java.util.ArrayList;
import java.util.List;

public class BankAccountResourceFromEntityAssembler {

    public static BankAccountResource toResourceFromEntity(BankAccount bankAccount) {
        return new BankAccountResource(
                bankAccount.getId(),
                bankAccount.getBankName(),
                bankAccount.getAccountNumber(),
                bankAccount.getAccountType().getType().name(),
                bankAccount.getCurrency(),
                bankAccount.getCreatedAt().toString(),
                bankAccount.getTransactions().stream().map(BankAccountResourceFromEntityAssembler::getTransactionFromEntity).toList()
        );
    }

    public static List<BankAccountTypeResource> toResourceFromEntity(List<BankAccountType> list) {

        List<BankAccountTypeResource> result = new ArrayList<>();

        for (BankAccountType entity : list) {
            BankAccountTypeResource t = new BankAccountTypeResource(
                    entity.getId(),
                    entity.getType().name()
            );

            result.add(t);
        }

        return result;
    }

    private static TransactionResource getTransactionFromEntity(Transaction transaction) {
        return new TransactionResource(
                transaction.getAmount(),
                transaction.getTransactionDate()
        );
    }

}
