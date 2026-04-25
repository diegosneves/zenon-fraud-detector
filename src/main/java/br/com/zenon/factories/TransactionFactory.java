package br.com.zenon.factories;

import br.com.zenon.enums.TransactionType;
import br.com.zenon.fraud.Transaction;
import br.com.zenon.fraud.TransactionCustomer;

import java.math.BigDecimal;

public final class TransactionFactory {

    private static final String TRUE_STRING = "1";

    private TransactionFactory() {}

    public static Transaction create(
            final Integer step,
            final TransactionType type,
            final Double amount,
            final String nameOrig,
            final Double oldBalanceOrig,
            final Double newBalanceOrig,
            final String nameDest,
            final Double oldBalanceDest,
            final Double newBalanceDest,
            final Boolean isFraud,
            final Boolean isFlaggedFraud
    ) {
        return Transaction.create(
                step,
                type,
                new BigDecimal(amount.toString()),
                TransactionCustomer.of(nameOrig, oldBalanceOrig.toString(), newBalanceOrig.toString()),
                TransactionCustomer.of(nameDest, oldBalanceDest.toString(), newBalanceDest.toString()),
                isFraud,
                isFlaggedFraud
        );
    }

    public static Transaction create(
            final Integer step,
            final TransactionType type,
            final String amount,
            final String nameOrig,
            final String oldBalanceOrig,
            final String newBalanceOrig,
            final String nameDest,
            final String oldBalanceDest,
            final String newBalanceDest,
            final Boolean isFraud,
            final Boolean isFlaggedFraud
    ) {
        return Transaction.create(
                step,
                type,
                new BigDecimal(amount),
                TransactionCustomer.of(nameOrig, oldBalanceOrig, newBalanceOrig),
                TransactionCustomer.of(nameDest, oldBalanceDest, newBalanceDest),
                isFraud,
                isFlaggedFraud
        );
    }

    public static Transaction create(
            final String step,
            final String type,
            final String amount,
            final String nameOrig,
            final String oldBalanceOrig,
            final String newBalanceOrig,
            final String nameDest,
            final String oldBalanceDest,
            final String newBalanceDest,
            final String isFraud,
            final String isFlaggedFraud
    ) {
        return Transaction.create(
                Integer.valueOf(step),
                TransactionType.valueOf(type),
                new BigDecimal(amount),
                TransactionCustomer.of(nameOrig, oldBalanceOrig, newBalanceOrig),
                TransactionCustomer.of(nameDest, oldBalanceDest, newBalanceDest),
                TRUE_STRING.equals(isFraud),
                TRUE_STRING.equals(isFlaggedFraud)
        );
    }

}
