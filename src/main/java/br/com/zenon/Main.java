package br.com.zenon;


import br.com.zenon.exceptions.DomainException;
import br.com.zenon.exceptions.ErrorDetail;
import br.com.zenon.exceptions.TransactionIngestorConstraintsException;
import br.com.zenon.fraud.FraudAnalyzer;
import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;
import br.com.zenon.validations.handlers.NotificationHandler;
import br.com.zenon.validations.handlers.ValidationHandler;

import java.util.List;
import java.util.Map;

public class Main {

    void main() {

        final var displayLimit = 0;
        final var listSize = 50_000;
        final var fileMap = Map.of(
                1, "data/PS_20174392719_1491204439457_log.csv",
                2, "data/wrong_file.csv"
        );

        final var loadedTransactions = recoveryTransactionByFile(fileMap.get(1), listSize, displayLimit);

        final var fraudAnalysis = FraudAnalyzer.of(loadedTransactions, Transaction::isFraud);
        fraudAnalysis.printFraudAnalysis();

    }

    private static List<Transaction> recoveryTransactionByFile(
            final String fileName,
            final int listSize,
            final int displayLimit
    ) {
        try {
            ValidationHandler notificationProcessor = NotificationHandler.create();
            final var ingester = TransactionIngestor.create(notificationProcessor, fileName, listSize);

            List<Transaction> transactionRecords = ingester.getTransactions();

            if (displayLimit > 0) {
                transactionRecords.stream().limit(displayLimit).forEach(IO::println);
                displayResult(ingester, transactionRecords, displayLimit, notificationProcessor);
            }
            return transactionRecords;
        } catch (final DomainException e) {
            System.err.println(e.getLocalizedMessage());
            e.getErrors().forEach(System.err::println);
            throw TransactionIngestorConstraintsException.with(ErrorDetail.of("File", "Invalid file format", Main.class));
        }
    }

    private static void displayResult(
            final TransactionIngestor ingester,
            final List<Transaction> transactionRecords,
            final int displayLimit,
            final ValidationHandler notificationProcessor
    ) {
        IO.println("""
                %n[%,03d] - Total of fraud transactions processed with success
                [%,03d] - Total found transactions
                [%,03d] - Total displayed transactions
                [%,03d] - Errors found
                """.formatted(
                ingester.getTransactions().size(),
                transactionRecords.size(),
                Math.min(transactionRecords.size(), displayLimit),
                notificationProcessor.getErrors().size()
        ));

        if (notificationProcessor.hasErrors()) {
            List<ErrorDetail> processorErrors = notificationProcessor.getErrors();
            if (!processorErrors.isEmpty()) {
                IO.println("%n[%s] - Errors found during processing:".formatted(processorErrors.size()));
                processorErrors.stream().limit(displayLimit).forEach(System.err::println);
            }
        }
    }
}
