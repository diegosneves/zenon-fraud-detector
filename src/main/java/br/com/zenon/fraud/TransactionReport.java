package br.com.zenon.fraud;

import br.com.zenon.enums.LocaleType;
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
    private final LocaleType localeType;

    private TransactionReport(final String filePath, final Integer skipLines, final LocaleType localeType) {
        this.filePath = filePath;
        this.skipLines = skipLines;
        this.localeType = localeType == null ? LocaleType.PT_BR : localeType;
    }

    public static TransactionReport of(final String filePath, final Boolean hasHeader, final LocaleType localeType) {
        return new TransactionReport(filePath, hasHeader ? 1 : 0, localeType);
    }

    public static TransactionReport of(final String filePath, final Boolean hasHeader) {
        return new TransactionReport(filePath, hasHeader ? 1 : 0, LocaleType.PT_BR);
    }

    public static TransactionReport of(final String filePath) {
        return TransactionReport.of(filePath, true);
    }

    public static TransactionReport of(final String filePath, final LocaleType localeType) {
        return TransactionReport.of(filePath, true, localeType);
    }


    public void generateReport() {
        var statistics = streamTransactionData();

        IO.println("""
                
                %s: %s
                %s: %s
                %s: %s
                """.formatted(
                this.localeType.fetchTranslation("total.lines.in.the.report"),
                this.localeType.formatNumber(statistics.totalTransactions()),
                this.localeType.fetchTranslation("total.fraudulent.transactions"),
                this.localeType.formatNumber(statistics.totalFraudulentTransactions()),
                this.localeType.fetchTranslation("total.amount.of.fraudulent.transactions"),
                this.localeType.convertToCurrency(statistics.totalFraudulentAmount(), "USD")
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
