package br.com.zenon.validations.handlers;

import br.com.zenon.exceptions.ErrorDetail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationHandler implements ValidationHandler {


    private final List<ErrorDetail> errors;


    private NotificationHandler(final List<ErrorDetail> errors) {
        final List<ErrorDetail> errorDetailList = new ArrayList<>();
        if (errors != null) {
            errorDetailList.addAll(errors);
        }
        this.errors = errorDetailList;
    }

    public static NotificationHandler create(final List<ErrorDetail> errors) {
        return new NotificationHandler(errors);
    }

    public static NotificationHandler create() {
        return create(new ArrayList<>());
    }

    public static NotificationHandler create(final ErrorDetail anError) {
        final List<ErrorDetail> errors = new ArrayList<>();
        if (anError != null) {
            errors.add(anError);
        }
        return create(errors);
    }

    public static NotificationHandler create(final Throwable throwable) {
        return create(ErrorDetail.of(throwable.getLocalizedMessage()));
    }

    @Override
    public ValidationHandler append(final ErrorDetail anError) {
        if (anError != null) {
            this.errors.add(anError);
        }
        return this;
    }

    @Override
    public ValidationHandler append(final String field, final String message) {
        if (message != null && !message.isBlank()) {
            this.append(ErrorDetail.of(field, message));
        }
        return this;
    }

    @Override
    public ValidationHandler append(final ValidationHandler anotherHandler) {
        if (anotherHandler != null && anotherHandler.hasErrors()) {
            this.errors.addAll(anotherHandler.getErrors());
        }
        return this;
    }

    @Override
    public List<ErrorDetail> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }
}
