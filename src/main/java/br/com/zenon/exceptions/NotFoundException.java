package br.com.zenon.exceptions;

import java.util.List;

public class NotFoundException extends DomainException {

    public static final String DEFAULT_ERROR_MESSAGE = "Resource not found";

    protected NotFoundException(String message, List<ErrorDetail> errors) {
        super(message, errors);
    }

    public static NotFoundException with(final List<ErrorDetail> errors) {
        return new NotFoundException(DEFAULT_ERROR_MESSAGE, errors);
    }

    public static NotFoundException with(final ErrorDetail anError) {
        return with(anError.toList());
    }

}
