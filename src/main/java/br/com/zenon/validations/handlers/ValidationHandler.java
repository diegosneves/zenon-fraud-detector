package br.com.zenon.validations.handlers;

import br.com.zenon.exceptions.ErrorDetail;

import java.util.List;
import java.util.function.Predicate;

public interface ValidationHandler {

    ValidationHandler append(final ErrorDetail anError);

    ValidationHandler append(final String field, final String message);

    ValidationHandler append(final String field, final String message, final Class<?> source);

    ValidationHandler append(final ValidationHandler anotherHandler);

    List<ErrorDetail> getErrors();

    List<ErrorDetail> getErrors(Predicate<ErrorDetail> filter);

    default Boolean hasErrors() {
        return this.getErrors() != null && !this.getErrors().isEmpty();
    }

    default ErrorDetail findFirst() {
        return this.hasErrors() ? this.getErrors().stream().findFirst().orElse(null) : null;
    }
}
