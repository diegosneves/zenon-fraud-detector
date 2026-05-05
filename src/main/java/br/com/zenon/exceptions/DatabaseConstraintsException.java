package br.com.zenon.exceptions;

import java.util.List;

public class DatabaseConstraintsException extends DomainException {

    public static final String DEFAULT_ERROR_MESSAGE = "Database constraints violated";

    protected DatabaseConstraintsException(String message, List<ErrorDetail> errors) {
        super(message, errors);
    }

    public static DatabaseConstraintsException with(final List<ErrorDetail> errors) {
        return new DatabaseConstraintsException(DEFAULT_ERROR_MESSAGE, errors);
    }

    public static DatabaseConstraintsException with(final ErrorDetail anError) {
        return with(anError.toList());
    }

}
