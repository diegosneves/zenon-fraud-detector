package br.com.zenon.fraud;

import br.com.zenon.enums.TransactionType;

import java.math.BigDecimal;

public record Transaction(
        Integer step,
        TransactionType type,
        BigDecimal amount,
        String nameOrig,
        BigDecimal oldBalanceOrig,
        BigDecimal newBalanceOrig,
        String nameDest,
        BigDecimal oldBalanceDest,
        BigDecimal newBalanceDest,
        Boolean isFraud,
        Boolean isFlaggedFraud
) {
}
