package com.transactionsapp.entities;

import com.transactionsapp.dto.TransactionDto;
import com.transactionsapp.dto.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


// Transaction entity class used for data exchange with the database.

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int number;
    @Enumerated(EnumType.STRING)
    private Type type;
    private double amount;
    private String description;
    private LocalDate date;

    public Transaction (Type type, double amount, String description, LocalDate date) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    public static Transaction of(TransactionDto transactionDto) {
        return Transaction.builder()
                .type(transactionDto.getType())
                .amount(transactionDto.getAmount())
                .description(transactionDto.getDescription())
                .date(transactionDto.getDate())
                .build();
    }
}
