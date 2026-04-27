package br.com.zenon.validations;

import br.com.zenon.enums.TransactionType;
import br.com.zenon.validations.handlers.ValidationHandler;

import java.util.Arrays;

public final class TransacionIngestorValidator extends Validator {

    private static final String DECIMAL_POSITIVE_REGEX_PATTERN = "\\d+(\\.\\d+)?";
    private static final String INTEGER_POSITIVE_REGEX_PATTERN = "\\d+";

    private final String[] fields;

    private TransacionIngestorValidator(final ValidationHandler validationHandler, final String[] fields) {
        super(validationHandler);
        this.fields = fields;
    }

    public static TransacionIngestorValidator of(final ValidationHandler validationHandler, final String[] fields) {
        return new TransacionIngestorValidator(validationHandler, fields);
    }

    @Override
    public void validate() {
        final String step = this.fields[0];
        final String type = this.fields[1];
        final String amount = this.fields[2];
        final String nameOrig = this.fields[3];
        final String oldBalanceOrig = this.fields[4];
        final String newBalanceOrig = this.fields[5];
        final String nameDest = this.fields[6];
        final String oldBalanceDest = this.fields[7];
        final String newBalanceDest = this.fields[8];
        final String isFraud = this.fields[9];
        final String isFlaggedFraud = this.fields[10];

        this.checkIfStepIsValid(step);
        this.checkIfTypeIsValid(type);
        this.checkIfAmountIsValid(amount);
        this.checkIfNameOrigIsValid(nameOrig);
        this.checkIfOldBalanceOrigIsValid(oldBalanceOrig);
        this.checkIfNewBalanceOrigIsValid(newBalanceOrig);
        this.checkIfNameDestIsValid(nameDest);
        this.checkIfOldBalanceDestIsValid(oldBalanceDest);
        this.checkIfNewBalanceDestIsValid(newBalanceDest);
        this.checkIfIsFraudIsValid(isFraud);
        this.checkIfIsFlaggedFraudIsValid(isFlaggedFraud);
    }

    private void checkIfIsFlaggedFraudIsValid(final String isFlaggedFraud) {
        if (isNullOrBlank("isFlaggedFraud", isFlaggedFraud)) return;

        if (!isFlaggedFraud.matches(INTEGER_POSITIVE_REGEX_PATTERN)) {
            this.getValidationHandler().append("isFlaggedFraud", "Is flagged fraud [%s] must be a valid boolean value".formatted(isFlaggedFraud), this.getClass());
        }
    }

    private void checkIfIsFraudIsValid(final String isFraud) {
        if (isNullOrBlank("isFraud", isFraud)) return;

        // Fraud precisa ser um numero inteiro 0 ou 1 para representar um booleano
        if (!isFraud.matches(INTEGER_POSITIVE_REGEX_PATTERN)) {
            this.getValidationHandler().append("isFraud", "Is fraud [%s] must be a valid boolean value".formatted(isFraud), this.getClass());
        }
    }

    private void checkIfNewBalanceDestIsValid(final String newBalanceDest) {
        if (isNullOrBlank("newBalanceDest", newBalanceDest)) return;

        if (!newBalanceDest.matches(DECIMAL_POSITIVE_REGEX_PATTERN)) {
            this.getValidationHandler().append("newBalanceDest", "New balance destination [%s] must be a valid number".formatted(newBalanceDest), this.getClass());
        }
    }

    private void checkIfOldBalanceDestIsValid(final String oldBalanceDest) {
        if (isNullOrBlank("oldBalanceDest", oldBalanceDest)) return;

        if (!oldBalanceDest.matches(DECIMAL_POSITIVE_REGEX_PATTERN)) {
            this.getValidationHandler().append("oldBalanceDest", "Old balance destination [%s] must be a valid number".formatted(oldBalanceDest), this.getClass());
        }
    }

    private void checkIfNameDestIsValid(final String nameDest) {
        isNullOrBlank("nameDest", nameDest);
    }

    private void checkIfNewBalanceOrigIsValid(final String newBalanceOrig) {
        if (isNullOrBlank("newBalanceOrig", newBalanceOrig)) return;

        if (!newBalanceOrig.matches(DECIMAL_POSITIVE_REGEX_PATTERN)) {
            this.getValidationHandler().append("newBalanceOrig", "New balance origin [%s] must be a valid number".formatted(newBalanceOrig), this.getClass());
        }
    }

    private void checkIfOldBalanceOrigIsValid(final String oldBalanceOrig) {
        if (this.isNullOrBlank("oldBalanceOrig", oldBalanceOrig)) return;

        if (!oldBalanceOrig.matches(DECIMAL_POSITIVE_REGEX_PATTERN)) {
            this.getValidationHandler().append("oldBalanceOrig", "Old balance origin [%s] must be a valid number".formatted(oldBalanceOrig), this.getClass());
        }
    }

    private void checkIfNameOrigIsValid(final String nameOrig) {
        this.isNullOrBlank("nameOrig", nameOrig);
    }

    private void checkIfStepIsValid(final String step) {
        if (this.isNullOrBlank("step", step)) return;
        //Step precisa ser um número inteiro positivo maior ou igual a 1
        if (!step.matches(INTEGER_POSITIVE_REGEX_PATTERN)) {
            this.getValidationHandler().append("step", "Step [%s] must be a positive integer".formatted(step), this.getClass());
        }
        if (step.matches(INTEGER_POSITIVE_REGEX_PATTERN) && Integer.parseInt(step) < 1) {
            this.getValidationHandler().append("step", "Step [%s] must be greater than or equal to 1".formatted(step), this.getClass());
        }

    }

    private void checkIfTypeIsValid(final String type) {
        if (this.isNullOrBlank("type", type)) return;

        try {
            TransactionType.valueOf(type);
        } catch (IllegalArgumentException _) {
            this.getValidationHandler().append("type", "Type [%s] must be one of: %s".formatted(type, Arrays.toString(TransactionType.values())), this.getClass());
        }
    }

    private void checkIfAmountIsValid(final String amount) {
        if (this.isNullOrBlank("amount", amount)) return;
        //Amount precisa ser um número decimal positivo e pode ser zero
        if (!amount.matches(DECIMAL_POSITIVE_REGEX_PATTERN)) {
            this.getValidationHandler().append("amount", "Amount [%s] must be a positive decimal number or zero".formatted(amount), this.getClass());
        }
    }

}
