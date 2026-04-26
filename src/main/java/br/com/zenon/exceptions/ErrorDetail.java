package br.com.zenon.exceptions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public record ErrorDetail(
        String field,
        String message,
        Instant timestamp
) {

    public static ErrorDetail of(final String field, final String message) {
        return new ErrorDetail(field, message, Instant.now());
    }

    public static ErrorDetail of(final String message) {
        return ErrorDetail.of(null, message);
    }

    public List<ErrorDetail> toList() {
        return List.of(this);
    }

    @Override
    public String toString() {
        final var localizedTimestamp = LocalDateTime.ofInstant(
                timestamp,
                ZoneId.systemDefault() //.of("America/Sao_Paulo") Tbm funciona
        );
        StringBuilder sb = new StringBuilder("ErrorDetail[");
        if (field != null && !field.isBlank()) {
            sb.append("field=").append(field).append(", ");
        }
        sb.append("message=").append(message).append(", ");
        sb.append("timestamp=").append(localizedTimestamp).append("]");
        return sb.toString();
    }
}
