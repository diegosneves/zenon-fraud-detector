package br.com.zenon.fraud;

import br.com.zenon.exceptions.ErrorDetail;
import br.com.zenon.exceptions.TransactionIngestorConstraintsException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TransactionReport {

    private final String filePath;
    private final Integer skipLines;

    private TransactionReport(final String filePath, final Integer skipLines) {
        this.filePath = filePath;
        this.skipLines = skipLines;
    }

    public static TransactionReport of(final String filePath, final Boolean hasHeader) {
        return new TransactionReport(filePath, hasHeader ? 1 : 0);
    }

    public static TransactionReport of(final String filePath) {
        return TransactionReport.of(filePath, true);
    }


    public void printReport() {
        long totalLines = streamTransactionData()
                .count();

        long totalFraudulent = streamTransactionData()
                .map(l -> l.split(","))
                .filter(parts -> parts[9].equals("1"))
                .count();

        BigDecimal totalAmount = streamTransactionData()
                .map(l -> l.split(","))
                .map(parts -> parseBigDecimal(parts[2]))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        IO.println("Total lines in report: %,d".formatted(totalLines));
        IO.println("Total fraudulent transactions: %,d".formatted(totalFraudulent));
        IO.println("Total amount of fraudulent transactions: %,.2f".formatted(totalAmount));
    }

    private Stream<String> streamTransactionData() {
        try {
            return Files.lines(Path.of(this.filePath)).skip(this.skipLines);
        } catch (IOException e) {
            throw TransactionIngestorConstraintsException.with(ErrorDetail.of("filePath", e.getMessage(), this.getClass()));
        }
    }

    private static BigDecimal parseBigDecimal(final String value) {
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException _) {
            return BigDecimal.valueOf(Double.parseDouble(value)).stripTrailingZeros();
        }
    }

}
