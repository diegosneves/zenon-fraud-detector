package br.com.zenon.validations.handlers;

import br.com.zenon.exceptions.ErrorDetail;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(final ErrorDetail anError);

    ValidationHandler append(final String field, final String message);

    ValidationHandler append(final ValidationHandler anotherHandler);

    List<ErrorDetail> getErrors();

    default Boolean hasErrors() {
        return this.getErrors() != null && !this.getErrors().isEmpty();
    }

    default ErrorDetail findFirst() {
        return this.hasErrors() ? this.getErrors().stream().findFirst().orElse(null) : null;
    }
}
