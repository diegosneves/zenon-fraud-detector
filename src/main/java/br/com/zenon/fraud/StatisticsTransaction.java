package br.com.zenon.fraud;

import java.math.BigDecimal;

public record StatisticsTransaction(
        Long totalTransactions,
        Long totalFraudulentTransactions,
        BigDecimal totalFraudulentAmount
) {

    public static StatisticsTransaction empty() {
        return new StatisticsTransaction(0L, 0L, BigDecimal.ZERO);
    }

    public StatisticsTransaction aggregate(final ReportTransaction reportTransaction) {
        return new StatisticsTransaction(
                this.totalTransactions + 1,
                this.totalFraudulentTransactions + (reportTransaction.isFraud() ? 1 : 0),
                this.totalFraudulentAmount.add(reportTransaction.amount())
        );
    }

    public StatisticsTransaction merge(final StatisticsTransaction other) {
        return new StatisticsTransaction(
                this.totalTransactions + other.totalTransactions,
                this.totalFraudulentTransactions + other.totalFraudulentTransactions,
                this.totalFraudulentAmount.add(other.totalFraudulentAmount)
        );
    }

}
