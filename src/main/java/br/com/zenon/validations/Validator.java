package br.com.zenon.validations;

import br.com.zenon.exceptions.ErrorDetail;
import br.com.zenon.validations.handlers.ValidationHandler;

public abstract class Validator {

    private final ValidationHandler validationHandler;

    protected Validator(final ValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
    }

    public abstract void validate();

    public ValidationHandler getValidationHandler() {
        return this.validationHandler;
    }

    protected Boolean isNullOrBlank(final String field, final String fieldValue) {
        var isInvalid = Boolean.FALSE;
        if (fieldValue == null || fieldValue.isBlank()) {
            this.validationHandler.append(ErrorDetail.of(field, String.format("Field '%s' cannot be null or empty", field)));
            isInvalid = Boolean.TRUE;
        }
        return isInvalid;
    }

}
