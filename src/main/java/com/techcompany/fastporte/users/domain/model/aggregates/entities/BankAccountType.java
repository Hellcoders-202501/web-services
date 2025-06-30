package com.techcompany.fastporte.users.domain.model.aggregates.entities;

import com.techcompany.fastporte.users.domain.model.aggregates.enums.AccountType;
import jakarta.persistence.*;

import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "bank_account_type")
@Data
public class BankAccountType implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type")
    private AccountType type;
}
