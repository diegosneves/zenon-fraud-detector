package br.com.zenon.exceptions;

import java.util.ArrayList;
import java.util.List;

public class DomainException extends RuntimeException {

    public static final String DEFAULT_ERROR_MESSAGE = "Domain validation error";

    private final List<ErrorDetail> errors;

    protected DomainException(final String message, final List<ErrorDetail> errors) {
        super(message);
        final List<ErrorDetail> errorDetailList = new ArrayList<>();
        if (errors != null) {
            errorDetailList.addAll(errors);
        }
        this.errors = errorDetailList;
    }

    public static DomainException with(final List<ErrorDetail> errors) {
        return new DomainException(DEFAULT_ERROR_MESSAGE, errors);
    }

    public static DomainException with(final ErrorDetail anError) {
        return with(anError.toList());
    }

    public List<ErrorDetail> getErrors() {
        return this.errors;
    }

}
