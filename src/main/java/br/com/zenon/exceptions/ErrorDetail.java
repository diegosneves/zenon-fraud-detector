package br.com.zenon.exceptions;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public record ErrorDetail(
        String field,
        String message,
        String source,
        Instant timestamp
) {

    public static ErrorDetail of(final String field, final String message, final Class<?> source) {
        String sourceData = null;
        if (source != null) {
            sourceData = StackWalker.getInstance().walk(
                    frames -> frames
                            .filter(f -> f.getClassName().equals(source.getName()))
                            .findFirst()
                            .map(f -> "%s#%s:%s".formatted(f.getClassName(), f.getMethodName(), f.getLineNumber()))
                            .orElse(source.getName())
            );
        }

        return new ErrorDetail(field, message, sourceData, Instant.now());
    }

    public static ErrorDetail of(final String field, final String message) {
        return ErrorDetail.of(field, message, null);
    }

    public static ErrorDetail of(final String message) {
        return ErrorDetail.of(null, message);
    }

    public static ErrorDetail of(final String message, final Class<?> source) {
        return ErrorDetail.of(null, message, source);
    }

    public List<ErrorDetail> toList() {
        return List.of(this);
    }

    @Override
    public String toString() {
        final var localizedTimestamp = LocalDateTime.ofInstant(
                this.timestamp,
                ZoneId.systemDefault() //.of("America/Sao_Paulo") Tbm funciona
        );
        StringBuilder sb = new StringBuilder("ErrorDetail[ ");
        if (this.source != null && !this.source.isBlank()) {
            sb.append("source= ").append(this.source).append(", ");
        }
        if (this.field != null && !this.field.isBlank()) {
            sb.append("field= ").append(this.field).append(", ");
        }
        sb.append("message= ").append(this.message).append(", ");
        sb.append("timestamp= ").append(localizedTimestamp).append(" ]");
        return sb.toString();
    }
}
