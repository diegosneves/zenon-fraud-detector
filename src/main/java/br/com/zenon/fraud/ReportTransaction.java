package br.com.zenon.fraud;

import java.math.BigDecimal;

public record ReportTransaction(
        BigDecimal amount,
        Boolean isFraud
) {


    public static ReportTransaction create(final BigDecimal amount, final Boolean isFraud) {
        return new ReportTransaction(amount, Boolean.TRUE.equals(isFraud));
    }

}
