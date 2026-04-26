package br.com.zenon.exceptions;

import java.util.List;

public class TransactionIngestorConstraintsException extends DomainException {

    public static final String DEFAULT_ERROR_MESSAGE = "Transaction ingestor constraints violated";

    protected TransactionIngestorConstraintsException(String message, List<ErrorDetail> errors) {
        super(message, errors);
    }

    public static TransactionIngestorConstraintsException with(final List<ErrorDetail> errors) {
        return new TransactionIngestorConstraintsException(DEFAULT_ERROR_MESSAGE, errors);
    }

    public static TransactionIngestorConstraintsException with(final ErrorDetail anError) {
        return with(anError.toList());
    }

}
