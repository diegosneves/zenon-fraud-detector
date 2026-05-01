package br.com.zenon.fraud;

import br.com.zenon.enums.TransactionType;

import java.math.BigDecimal;

public record Transaction(
        Integer step,
        TransactionType type,
        BigDecimal amount,
        TransactionCustomer origin,
        TransactionCustomer recipient,
        Boolean isFraud,
        Boolean isFlaggedFraud
) {

    public static Transaction create(
            final Integer step,
            final TransactionType type,
            final BigDecimal amount,
            final TransactionCustomer origin,
            final TransactionCustomer receiver,
            final Boolean isFraud,
            final Boolean isFlaggedFraud
    ) {
        return new Transaction(
                step,
                type,
                amount,
                origin,
                receiver,
                isFraud,
                isFlaggedFraud
        );
    }

    @Override
    public String toString() {
        return String.format(
                "Transaction{step=%d, type=%s, amount=%,.2f, origin=%s, recipient=%s, isFraud=%b, isFlaggedFraud=%b}",
                step, type, amount, origin, recipient, isFraud, isFlaggedFraud
        );
    }
}
