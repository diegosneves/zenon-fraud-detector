package br.com.zenon.fraud;

import java.math.BigDecimal;

public record TransactionCustomer(
        String name,
        BigDecimal oldBalance,
        BigDecimal newBalance
) {

    public static TransactionCustomer of(
            final String name,
            final BigDecimal oldBalance,
            final BigDecimal newBalance
    ) {
        return new TransactionCustomer(name, oldBalance, newBalance);
    }

    @Override
    public String toString() {
        return String.format(
                "TransactionCustomer{name='%s', oldBalance=%,.2f, newBalance=%,.2f}",
                name, oldBalance, newBalance
        );
    }

}
