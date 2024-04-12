package com.transactionsapp.components;

import com.transactionsapp.dto.Type;
import com.transactionsapp.entities.Transaction;
import com.transactionsapp.service.TransactionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private TransactionsService transactionsService;

    @Override
    public void run(String... args) throws Exception {
        initializeTransactions();
    }

    private void initializeTransactions() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(new Transaction(Type.CREDIT, 15000, "משכורת", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-01-01"))));
        transactions.add(new Transaction(Type.CREDIT, 300, "זכייה בפיס", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-01-06"))));
        transactions.add(new Transaction(Type.DEBIT, 130.56, "חיוב אשראי", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-01-01"))));
        transactions.add(new Transaction(Type.DEBIT, 1000, "הורדת צ'ק", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-01-03"))));
        transactions.add(new Transaction(Type.DEBIT, 235.60, "חיוב אשראי", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-01-01"))));
        transactions.add(new Transaction(Type.DEBIT, 100, "עמלה", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-01-02"))));
        transactions.add(new Transaction(Type.DEBIT, 1000, "משיכת מזומן", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-01-03"))));
        transactions.add(new Transaction(Type.CREDIT, 15432, "משכורת", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-02-01"))));
        transactions.add(new Transaction(Type.CREDIT, 5000, "העברה מאמא", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-02-10"))));
        transactions.add(new Transaction(Type.DEBIT, 130.56, "חיוב אשראי", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-02-01"))));
        transactions.add(new Transaction(Type.DEBIT, 2456, "הורדת צ'ק", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-02-03"))));
        transactions.add(new Transaction(Type.DEBIT, 235.60, "חיוב אשראי", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-02-01"))));
        transactions.add(new Transaction(Type.DEBIT, 93.43, "עמלה", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-02-02"))));
        transactions.add(new Transaction(Type.DEBIT, 1234, "משיכת מזומן", LocalDate.from(DateTimeFormatter.ISO_LOCAL_DATE.parse("2024-02-03"))));

        transactions.forEach(t -> transactionsService.addTransaction(t));
    }

}
