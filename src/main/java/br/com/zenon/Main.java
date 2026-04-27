package br.com.zenon;


import br.com.zenon.exceptions.DomainException;
import br.com.zenon.exceptions.ErrorDetail;
import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionIngestor;
import br.com.zenon.validations.handlers.NotificationHandler;
import br.com.zenon.validations.handlers.ValidationHandler;

import java.util.List;

public class Main {

    void main() {

        final var displayLimit = 16;
        final var listSize = 50_000;

        try {
            ValidationHandler notificationProcessor = NotificationHandler.create();
            final var ingester = TransactionIngestor.create(notificationProcessor, "data/PS_20174392719_1491204439457_log.csv", listSize);
//            final var ingester = TransactionIngestor.create(notificationProcessor,"data/wrong_file.csv", listSize);

            List<Transaction> transactionRecords = ingester.getTransactions();
//            List<Transaction> transactionRecords = ingester.getTransactions(transaction -> transaction.amount().compareTo(new BigDecimal("0.0")) > 0);

            transactionRecords.stream().limit(displayLimit).forEach(IO::println);

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
        } catch (final DomainException e) {
            System.err.println(e.getLocalizedMessage());
            e.getErrors().forEach(System.err::println);
        }

    }
}
