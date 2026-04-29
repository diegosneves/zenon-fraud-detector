package br.com.zenon.fraud;

import br.com.zenon.enums.TransactionType;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class FraudAnalyzer {

    private static final String REPORT_HEADER = " Fraud Analysis Report ";

    private final List<Transaction> transactions;

    private FraudAnalyzer(final List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public static FraudAnalyzer of(final List<Transaction> transactions, final Predicate<Transaction> filter) {
        Objects.requireNonNull(transactions, "transactions cannot be null");
        Objects.requireNonNull(filter, "filter cannot be null");
        return new FraudAnalyzer(transactions.stream().filter(filter).toList());
    }

    private static void generateReportHeader(final int headerWidth) {
        final String headerSymbol = "=";
        System.out.printf(("%n%s" + REPORT_HEADER + "%s%n%n"), headerSymbol.repeat(headerWidth), headerSymbol.repeat(headerWidth));
    }

    private static void separator(final int length) {
        System.out.println("-".repeat(length * 2 + REPORT_HEADER.length()));
    }

    public void printFraudAnalysis() {
        int width = 50;
        generateReportHeader(width);
        processTotalFraud();

        separator(width);
        processTopAmountFraud(3);
        separator(width);
        processSuspectedOrigCustomer(5);
        separator(width);
        processTotalLoss();
        separator(width);
        processFraudByTransactionType();
        separator(width);
    }

    private void processFraudByTransactionType() {
        Map<TransactionType, Long> fraudByType = this.transactions.stream()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));

        System.out.println("Fraud by transaction type:");
        fraudByType.forEach((type, count) -> System.out.printf("%s: %d%n", type, count));
    }

    private void processTotalLoss() {
        BigDecimal calculatedLoss = this.transactions.stream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.printf("Total loss from fraud: $%,.2f%n", calculatedLoss);
    }

    private void processSuspectedOrigCustomer(final int limit) {
        List<String> suspectedCustomers = this.sortTransactionsByAmountDesc()
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(limit)
                .toList();

        System.out.println("Suspected customers with high fraud activity:");
        AtomicInteger index = new AtomicInteger(1);
        suspectedCustomers.stream().forEach(transaction ->
                System.out.printf("%d. %s%n", index.getAndIncrement(), transaction)
        );
    }

    private void processTopAmountFraud(final int limit) {
        List<Transaction> top3AmountFraud = this.sortTransactionsByAmountDesc()
                .limit(limit)
                .toList();

        System.out.printf("Top %d fraud transactions by amount:%n", limit);
        AtomicInteger index = new AtomicInteger(1);
        top3AmountFraud.forEach(transaction ->
                System.out.printf("%d. $%,.2f%n", index.getAndIncrement(), transaction.amount())
        );
    }


    private void processTotalFraud() {
        final var totalFraud = this.transactions.stream().count();
        System.out.printf("Total fraud transactions: %n%s%n", totalFraud);
    }

    private Stream<Transaction> sortTransactionsByAmountDesc() {
        return this.transactions.stream()
                .sorted(Comparator.comparing(Transaction::amount).reversed());
    }
}
