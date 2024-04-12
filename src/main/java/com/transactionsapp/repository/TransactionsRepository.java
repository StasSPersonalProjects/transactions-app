package com.transactionsapp.repository;

import com.transactionsapp.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transaction, Integer> {

    @Query(value = "SELECT * FROM transactions t WHERE t.type = :type", nativeQuery = true)
    List<Transaction> findAllTransactionsByType(@Param("type") String type);

    @Query(value = "SELECT * FROM transactions t WHERE t.date BETWEEN :from AND :to", nativeQuery = true)
    List<Transaction> findAllBetweenDates(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query(value = "SELECT * FROM transactions t WHERE t.amount > :amount", nativeQuery = true)
    List<Transaction> findAllLargerThan(@Param("amount") Double amount);
}
