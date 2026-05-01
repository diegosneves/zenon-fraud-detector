package br.com.zenon.fraud;

import br.com.zenon.exceptions.ErrorDetail;
import br.com.zenon.exceptions.TransactionIngestorConstraintsException;
import br.com.zenon.fraud.factories.TransactionFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
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


    public void generateReport() {
        var statistics = streamTransactionData();

        IO.println("""
                
                Total lines in report: %,d
                Total fraudulent transactions: %,d
                Total amount of fraudulent transactions: %,.2f
                """.formatted(
                statistics.totalTransactions(),
                statistics.totalFraudulentTransactions(),
                statistics.totalFraudulentAmount()
        ));
    }

    private StatisticsTransaction streamTransactionData() {

        try (Stream<String> transactionLines = Files.lines(Path.of(this.filePath)).skip(this.skipLines)) {
            return transactionLines
                    .map(l -> l.split(","))
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .reduce(
                            StatisticsTransaction.empty(),
                            StatisticsTransaction::aggregate,
                            StatisticsTransaction::merge
                    );

        } catch (IOException e) {
            throw TransactionIngestorConstraintsException.with(ErrorDetail.of("filePath", e.getMessage(), this.getClass()));
        }
    }


    private Optional<ReportTransaction> parseTransaction(final String[] fields) {
        final String amount = fields[2];
        final String isFraud = fields[9];

        return Optional.of(TransactionFactory.createReportTransaction(amount, isFraud));
    }


}
