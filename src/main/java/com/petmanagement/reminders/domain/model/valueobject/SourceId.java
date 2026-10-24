package com.petmanagement.reminders.domain.model.valueobject;

import org.jmolecules.ddd.annotation.ValueObject;

import java.util.Objects;
import java.util.UUID;

@ValueObject
public record SourceId(UUID value) {

    public SourceId {
        Objects.requireNonNull(value, "Source id cannot be null");
    }

    public static SourceId of(UUID value) {
        return new SourceId(value);
    }

    public static SourceId of(String value) {
        Objects.requireNonNull(value, "Source id cannot be null");
        return new SourceId(UUID.fromString(value));
    }

}
