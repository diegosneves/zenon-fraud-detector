package br.com.zenon;


import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;

import java.util.List;

public class Main {

    void main() {

//        final var ingester = TransactionIngestor.create("data/PS_20174392719_1491204439457_log.csv");
        final var ingester = TransactionIngestor.create("data/PS_20174392719_1491204439457_log.csv", 1_000);

        List<Transaction> transactionRecords = ingester.getTransactions(10);

        transactionRecords.forEach(IO::println);

        IO.println("\n[%,03d] - Fraud transactions processed\n[%,03d] - Total found transactions".formatted(ingester.getTransactions().size(), transactionRecords.size()));

    }
}
