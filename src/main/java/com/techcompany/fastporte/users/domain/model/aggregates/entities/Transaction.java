package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
@Data
@AllArgsConstructor
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bank_account_id", nullable = false)
    private BankAccount bankAccount;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    public Transaction(BankAccount bankAccount, Double amount) {
        this.bankAccount = bankAccount;
        this.amount = amount;
        this.transactionDate = LocalDateTime.now().withNano(0);
    }

    public Transaction() {
        this.transactionDate = LocalDateTime.now().withNano(0);
    }
}