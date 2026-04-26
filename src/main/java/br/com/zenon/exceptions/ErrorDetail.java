package br.com.zenon.exceptions;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

public record ErrorDetail(
        String field,
        String message,
        Instant timestamp
) {

    public static ErrorDetail of(final String field, final String message) {
        final var currentTime = Instant.now();
        return new ErrorDetail(field, message, currentTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static ErrorDetail of(final String message) {
        return ErrorDetail.of(null, message);
    }

    public List<ErrorDetail> toList() {
        return List.of(this);
    }

}
