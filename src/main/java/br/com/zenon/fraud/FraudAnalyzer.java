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

public final class FraudAnalyzer {

    private static final String REPORT_HEADER = " Fraud Analysis Report ";

    private final List<Transaction> transactions;
    private final Predicate<Transaction> filter;

    private FraudAnalyzer(final List<Transaction> transactions, final Predicate<Transaction> filter) {
        this.transactions = transactions;
        this.filter = filter;
    }

    public static FraudAnalyzer of(final List<Transaction> transactions, final Predicate<Transaction> filter) {
        Objects.requireNonNull(transactions, "transactions cannot be null");
        Objects.requireNonNull(filter, "filter cannot be null");
        return new FraudAnalyzer(transactions, filter);
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
        processTop3AmountFraud();
        separator(width);
        processSuspectedOrigCustomer();
        separator(width);
        processTotalLoss();
        separator(width);
        processFraudByTransactionType();
        separator(width);
    }

    private void processFraudByTransactionType() {
        Map<TransactionType, Long> fraudByType = this.transactions.stream()
                .filter(this.filter)
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));

        System.out.println("Fraud by transaction type:");
        fraudByType.forEach((type, count) -> System.out.printf("%s: %d%n", type, count));
    }

    private void processTotalLoss() {
        this.transactions.stream()
                .filter(this.filter)
                .map(Transaction::amount)
                .reduce(BigDecimal::add)
                .ifPresent(totalLoss -> System.out.printf("Total loss from fraud: $%,.2f%n", totalLoss));
    }

    private void processSuspectedOrigCustomer() {
        List<String> suspectedCustomers = transactions.stream()
                .filter(this.filter)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(5)
                .toList();

        System.out.println("Suspected customers with high fraud activity:");
        AtomicInteger index = new AtomicInteger(1);
        suspectedCustomers.stream().forEach(transaction ->
                System.out.printf("%d. %s%n", index.getAndIncrement(), transaction)
        );
    }

    private void processTop3AmountFraud() {
        List<Transaction> top3AmountFraud = transactions.stream()
                .filter(this.filter)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .limit(3)
                .toList();

        System.out.println("Top 3 fraud transactions by amount:");
        AtomicInteger index = new AtomicInteger(1);
        top3AmountFraud.forEach(transaction ->
                System.out.printf("%d. $%,.2f%n", index.getAndIncrement(), transaction.amount())
        );
    }


    private void processTotalFraud() {
        final var totalFraud = transactions.stream().filter(this.filter).count();
        System.out.printf("Total fraud transactions: %n%s%n", totalFraud);
    }
}
