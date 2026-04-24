package br.com.zenon.fraud;

import java.math.BigDecimal;

public record TransactionCustomer(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {

    public static TransactionCustomer of(
            final String name,
            final String oldBalance,
            final String newBalance
    ) {
        return new TransactionCustomer(name, new BigDecimal(oldBalance), new BigDecimal(newBalance));
    }

}
