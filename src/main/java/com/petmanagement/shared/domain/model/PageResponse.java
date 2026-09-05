package com.petmanagement.shared.domain.model;

import java.util.List;
import java.util.Objects;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {

    public PageResponse {
        content = List.copyOf(Objects.requireNonNull(content, "Content cannot be null"));

        if (page < 0) throw new IllegalArgumentException("Page cannot be negative");
        if (size <= 0) throw new IllegalArgumentException("Size must be greater than zero");
        if (totalElements < 0) throw new IllegalArgumentException("Total elements cannot be negative");
    }

    public static <T> PageResponse<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = Math.toIntExact((totalElements + size - 1) / size);
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    public static <T> PageResponse<T> empty(int page, int size) {
        return of(List.of(), page, size, 0);
    }

    public int numberOfElements() {
        return content.size();
    }

    public boolean hasNext() {
        return page + 1 < totalPages;
    }

    public boolean hasPrevious() {
        return page > 0;
    }

    public boolean isFirst() {
        return page == 0;
    }

    public boolean isLast() {
        return totalPages == 0 || page >= totalPages - 1;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

}
