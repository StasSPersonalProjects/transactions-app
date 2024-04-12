package com.transactionsapp.service;

import com.transactionsapp.entities.Transaction;
import com.transactionsapp.repository.TransactionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Service class containing methods for manipulating the database.

@Service
public class TransactionsService {

    @Autowired
    private TransactionsRepository transactionsRepository;

    static Logger LOG = LoggerFactory.getLogger(TransactionsService.class);

    @Transactional
    public Transaction addTransaction(Transaction transaction) {
        LOG.debug("Adding new transaction, details: {}", transaction.toString());
        return transactionsRepository.save(transaction);
    }

    @Transactional
    public String updateTransaction(Integer id, Transaction transaction) {
        Optional<Transaction> transactionByProvidedId = transactionsRepository.findById(id);
        if (transactionByProvidedId.isPresent()) {
            LOG.debug("Updating transaction with ID {}.", id);
            Transaction transactionToUpdate = transactionByProvidedId.get();
            transactionToUpdate.setType(transaction.getType());
            transactionToUpdate.setAmount(transaction.getAmount());
            transactionToUpdate.setDescription(transaction.getDescription());
            transactionToUpdate.setDate(transaction.getDate());
            transactionsRepository.save(transactionToUpdate);
            return String.format("Transaction with ID %d was successfully updated!", id);
        } else {
            return String.format("Transaction with ID %d wasn't found!", id);
        }
    }

    @Transactional
    public String deleteTransaction(Integer id) {
        Optional<Transaction> transactionByProvidedId = transactionsRepository.findById(id);
        if (transactionByProvidedId.isPresent()) {
            LOG.debug("Deleting transaction with ID {}.", id);
            transactionsRepository.deleteById(id);
            return String.format("Transaction with ID %d was successfully deleted!", id);
        } else {
            return String.format("Transaction with ID %d wasn't found!", id);
        }
    }

    public List<Transaction> getAllTransactions() {
        LOG.debug("Fetching all transaction from the database.");
        return transactionsRepository.findAll();
    }

    public List<Transaction> getAllTransactionsByType(String type) {
        LOG.debug("Fetching all transaction of type {}.", type);
        return transactionsRepository.findAllTransactionsByType(type);
    }

    public List<Transaction> getAllTransactionsBetweenDates(LocalDate from, LocalDate to) {
        LOG.debug("Fetching all transaction from {} to {}.", from, to);
        return transactionsRepository.findAllBetweenDates(from, to);
    }

    public List<Transaction> getAllTransactionsLargerThanAmount(Double amount) {
        LOG.debug("Fetching all transaction of amount larger than {}.", amount);
        return transactionsRepository.findAllLargerThan(amount);
    }

    public Double getCurrentBalance() {
        LOG.debug("Calculating current account balance.");
        List<Transaction> creditTransactions = getAllTransactionsByType("CREDIT");
        List<Transaction> debitTransactions = getAllTransactionsByType("DEBIT");
        Double creditSum = creditTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(Double::sum).orElse((double) 0);
        Double debitSum = debitTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(Double::sum).orElse((double) 0);
        return creditSum - debitSum;
    }
}
