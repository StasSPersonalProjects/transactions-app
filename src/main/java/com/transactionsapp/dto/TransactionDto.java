package com.transactionsapp.dto;

import com.transactionsapp.entities.Transaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {

    @Pattern(regexp = "(?i)\\b(CREDIT|DEBIT)\\b")
    private Type type;
    @NotNull(message = "Transaction amount field must not be empty.")
    private double amount;
    private String description;
    private LocalDateTime date;

    public static TransactionDto of(Transaction transaction) {
        return TransactionDto.builder()
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .date(transaction.getDate())
                .build();
    }

}
