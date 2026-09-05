package com.petmanagement.shared.domain.model;

public record PageRequest(
        int page,
        int size
) {

    public PageRequest {
        if (page < 0)
            throw new IllegalArgumentException("Page cannot be negative");
        if (size <= 0)
            throw new IllegalArgumentException("Size must be greater than zero");
    }

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }

}
