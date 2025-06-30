package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bank_account")
@Data
@AllArgsConstructor
public class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "account_number", nullable = false, length = 50)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "account_type")
    private BankAccountType accountType;

    @Column(name = "currency", length = 10)
    private String currency;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public BankAccount(Driver driver, String bankName, String accountNumber, BankAccountType accountType) {
        this.driver = driver;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.currency = "PEN";
        this.createdAt = LocalDateTime.now().withNano(0);
    }

    public BankAccount() {
        this.currency = "PEN";
        this.createdAt = LocalDateTime.now().withNano(0);
    }
}