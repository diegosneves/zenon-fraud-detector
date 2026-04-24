package br.com.zenon;


import br.com.zenon.enums.TransactionType;
import br.com.zenon.factories.TransactionFactory;

public class Main {

    void main() {

        var transacao1 = TransactionFactory.create(
                1,
                TransactionType.PAYMENT,
                "100.0",
                "C1231006815",
                "1000.0",
                "900.0",
                "M1979787155",
                "900.0",
                "1000.0",
                Boolean.FALSE,
                Boolean.FALSE
        );

        var transacao2 = TransactionFactory.create(
                654,
                TransactionType.CASH_OUT,
                100.0,
                "C4331006815",
                1000.0,
                900.0,
                "M2179787153",
                900.0,
                1000.0,
                Boolean.FALSE,
                Boolean.FALSE
        );

        IO.println("\nTransação 1: %s, \n\nTransação 2: %s\n".formatted(transacao1, transacao2));

    }
}
