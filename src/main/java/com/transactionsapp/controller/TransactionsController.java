package com.transactionsapp.controller;

import com.transactionsapp.dto.TransactionDto;
import com.transactionsapp.entities.Transaction;
import com.transactionsapp.service.CurrencyService;
import com.transactionsapp.service.TransactionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


// Controller class for receiving requests,
// checking the data,
// proceeding to the service class and responding accordingly.

@RestController
@RequestMapping("/app/transactions")
@Validated
public class TransactionsController {

    @Autowired
    private TransactionsService transactionsService;
    @Autowired
    private CurrencyService currencyService;

    static Logger LOG = LoggerFactory.getLogger(TransactionsController.class);

    @PostMapping
    public ResponseEntity<String> addTransaction(@RequestBody TransactionDto transactionDto) {
        LOG.debug("Received request to add new transaction.");
        Transaction savedTransaction = transactionsService.addTransaction(Transaction.of(transactionDto));
        if (savedTransaction != null) {
            return ResponseEntity.ok("Transaction saved successfully!");
        } else {
            return ResponseEntity.internalServerError().body("Something went wrong... transaction wasn't saved.");
        }
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> updateTransaction(@PathVariable Integer id, @RequestBody TransactionDto transactionDto) {
        LOG.debug("Received request to update transaction with ID {}.", id);
        String result = transactionsService.updateTransaction(id, Transaction.of(transactionDto));
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTransAction(@PathVariable Integer id) {
        LOG.debug("Received request to delete transaction with ID {}.", id);
        String result = transactionsService.deleteTransaction(id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllTransactions() {
        LOG.debug("Received request to get all transactions.");
        List<Transaction> fetchedTransactions = transactionsService.getAllTransactions();
        var result = checkFetchedTransactions(fetchedTransactions);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/all/{type}")
    public ResponseEntity<?> getAllTransactionsByType(@PathVariable String type) {
        LOG.debug("Received request to get all transactions of type {}.", type);
        if (!type.matches("\\b(credit|debit)\\b")) {
            return ResponseEntity.badRequest().body("Wrong type request.");
        }
        return ResponseEntity.ok().body(transactionsService.getAllTransactionsByType(type));
    }

    @GetMapping(value = "/all/dates")
    public ResponseEntity<?> getAllTransactionsBetweenDates(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        LOG.debug("Received request to get all transactions from {} to {}.", from, to);
        if (from.isAfter(LocalDate.now()) || from.isAfter(to)) {
            LOG.debug("Wrong dates entered.");
            return ResponseEntity.badRequest().body("Please ensure that dates are entered correctly.");
        }
        if (to.isAfter(LocalDate.now())) {
            LOG.debug("{} is a future date, changing it to current date.", to);
            to = LocalDate.now();
        }
        List<Transaction> fetchedTransactions = transactionsService.getAllTransactionsBetweenDates(from, to);
        var result = checkFetchedTransactions(fetchedTransactions);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/amount")
    public ResponseEntity<?> getAllTransactionsLargerThanAmount(@RequestParam Double amount) {
        LOG.debug("Received request to get all transactions of amount larger than {}.", amount);
        if (amount < 0) {
            LOG.debug("The given amount is a negative number.");
            return ResponseEntity.badRequest().body("The amount must be larger than 0.");
        }
        List<Transaction> fetchedTransactions = transactionsService.getAllTransactionsLargerThanAmount(amount);
        var result = checkFetchedTransactions(fetchedTransactions);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "/balance")
    public Double getCurrentBalance() {
        LOG.debug("Received request to get current account balance.");
        return transactionsService.getCurrentBalance();
    }

    /**
     * @param fetchedTransactions fetched transactions, provided to check them and prepare a response
     * @return the result made of the provided fetched transactions
     * @param <T> the result might be of type Transaction or null
     * @throws ClassCastException
     */
    private <T> T checkFetchedTransactions(List<Transaction> fetchedTransactions) throws ClassCastException {
        LOG.debug("Found {} transactions.", fetchedTransactions.size());
        if (fetchedTransactions.isEmpty()) {
            return (T) "No transactions found!";
        }
        List<TransactionDto> result = fetchedTransactions
                .stream()
                .map(TransactionDto::of)
                .toList();
        return (T) result;
    }

    @GetMapping(value = "/convert")
    public ResponseEntity<Double> convertCurrency(@RequestParam Double amountInILS, @RequestParam String targetCurrency) throws Exception {
        LOG.debug("Received request to convert {} ILS to {}.", amountInILS, targetCurrency);
        return ResponseEntity.ok().body(currencyService.convertCurrency(amountInILS, targetCurrency));
    }
}
