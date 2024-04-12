package com.transactionsapp.entities;

import com.transactionsapp.dto.TransactionDto;
import com.transactionsapp.dto.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime date;

    public static Transaction of(TransactionDto transactionDto) {
        return Transaction.builder()
                .type(transactionDto.getType())
                .amount(transactionDto.getAmount())
                .description(transactionDto.getDescription())
                .date(transactionDto.getDate())
                .build();
    }
}
