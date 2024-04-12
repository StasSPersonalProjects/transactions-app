package com.transactionsapp.dto;

import com.transactionsapp.entities.Transaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// Data transfer object for transferring the transaction data.

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {

    @Pattern(regexp = "(?i)\\b(CREDIT|DEBIT)\\b", message = "Transaction must be of type credit or debit only!")
    private Type type;
    @NotNull(message = "Transaction amount field must not be empty.")
    private double amount;
    private String description;
    private LocalDate date;

    public static TransactionDto of(Transaction transaction) {
        return TransactionDto.builder()
                .type(transaction.getType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .date(transaction.getDate())
                .build();
    }

}
