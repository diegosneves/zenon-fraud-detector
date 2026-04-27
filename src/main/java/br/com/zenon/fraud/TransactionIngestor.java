package br.com.zenon.fraud;

import br.com.zenon.exceptions.ErrorDetail;
import br.com.zenon.exceptions.TransactionIngestorConstraintsException;
import br.com.zenon.fraud.factories.TransactionFactory;
import br.com.zenon.validations.TransacionIngestorValidator;
import br.com.zenon.validations.handlers.NotificationHandler;
import br.com.zenon.validations.handlers.ValidationHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TransactionIngestor {

    private static final Logger logger = Logger.getLogger(TransactionIngestor.class.getName());

    private static final String DEFAULT_DELIMITER = ",";

    private final List<Transaction> transactions;

    private TransactionIngestor(final List<Transaction> transactions) {
        this.transactions = transactions == null ? new ArrayList<>() : transactions;
    }

    public static TransactionIngestor create(
            final ValidationHandler validationHandler,
            final String fileName,
            final String delimiter,
            final Boolean skipHeader,
            final Integer limit
    ) {
        List<Transaction> transactions;

        Path filePath = Path.of(fileName);

        try {
            List<String> fileContents = Files.readAllLines(filePath);

            transactions = fileContents.stream()
                    .skip(Boolean.TRUE.equals(skipHeader) ? 1 : 0)
                    .limit(limit == null ? fileContents.size() : limit)
                    .map(line -> line.trim().split(delimiter))
                    .filter(fields -> fields.length == 11)
                    .map(fields -> TransactionIngestor.parseTransaction(validationHandler, fields))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

        } catch (IOException e) {
            final var error = ErrorDetail.of("fileName", "Erro ao ler arquivo: %s".formatted(fileName), TransactionIngestor.class);
            logger.log(Level.SEVERE, error.toString(), e);
            throw TransactionIngestorConstraintsException.with(error);
        } catch (Exception e) {
            final var error = ErrorDetail.of("Erro inesperado ao ler arquivo: %s".formatted(fileName), TransactionIngestor.class);
            logger.log(Level.SEVERE, error.toString(), e);
            throw TransactionIngestorConstraintsException.with(error);
        }

        return new TransactionIngestor(transactions);
    }

    private static Optional<Transaction> parseTransaction(final ValidationHandler validationHandler, final String[] fields) {
        NotificationHandler notifier = NotificationHandler.create();
        validationFields(notifier, fields);

        if (notifier.hasErrors()) {
            validationHandler.append(notifier);
            return Optional.empty();
        }

        final String step = fields[0];
        final String type = fields[1];
        final String amount = fields[2];
        final String nameOrig = fields[3];
        final String oldBalanceOrig = fields[4];
        final String newBalanceOrig = fields[5];
        final String nameDest = fields[6];
        final String oldBalanceDest = fields[7];
        final String newBalanceDest = fields[8];
        final String isFraud = fields[9];
        final String isFlaggedFraud = fields[10];

        return Optional.of(TransactionFactory.create(
                step,
                type,
                amount,
                nameOrig,
                oldBalanceOrig,
                newBalanceOrig,
                nameDest,
                oldBalanceDest,
                newBalanceDest,
                isFraud,
                isFlaggedFraud
        ));
    }

    private static void validationFields(final ValidationHandler validationHandler, final String[] fields) {
        final var validator = TransacionIngestorValidator.of(validationHandler, fields);
        validator.validate();
    }

    public static TransactionIngestor create(final ValidationHandler validationHandler, final String fileName) {
        return create(validationHandler, fileName, DEFAULT_DELIMITER, Boolean.TRUE, null);
    }

    public static TransactionIngestor create(final ValidationHandler validationHandler, final String fileName, final Integer limit) {
        return create(validationHandler, fileName, DEFAULT_DELIMITER, Boolean.TRUE, limit);
    }

    public static TransactionIngestor create(final ValidationHandler validationHandler, final String fileName, final Boolean skipHeader, final Integer limit) {
        return create(validationHandler, fileName, DEFAULT_DELIMITER, skipHeader, limit);
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public List<Transaction> getTransactions(final Predicate<Transaction> filter) {
        return this.getTransactions(filter, null);
    }

    public List<Transaction> getTransactions(final Predicate<Transaction> filter, final Integer limit) {
        return this.transactions.stream()
                .filter(filter)
                .limit(limit == null ? this.transactions.size() : limit)
                .toList();
    }

    public List<Transaction> getTransactions(final Integer limit) {
        return this.transactions.stream()
                .limit(limit == null ? this.transactions.size() : limit)
                .toList();
    }

}
