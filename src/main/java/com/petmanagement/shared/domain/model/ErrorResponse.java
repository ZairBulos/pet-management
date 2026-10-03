package com.petmanagement.shared.domain.model;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record ErrorResponse(
        String error,
        String message,
        List<String> details,
        String timestamp,
        String path
) {

    public ErrorResponse {
        Objects.requireNonNull(error, "Error cannot be null");
        Objects.requireNonNull(message, "Message  cannot be null");
        Objects.requireNonNull(timestamp, "Timestamp cannot be null");
        Objects.requireNonNull(path, "Path cannot be null");
    }

    public static ErrorResponse of(
            String error,
            String message,
            String path
    ) {
        return new ErrorResponse(
                error,
                message,
                null,
                Instant.now().toString(),
                path
        );
    }

    public static ErrorResponse of(
            String error,
            String message,
            List<String> details,
            String path
    ) {
        return new ErrorResponse(
                error,
                message,
                details,
                Instant.now().toString(),
                path
        );
    }

}
