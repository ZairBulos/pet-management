package com.petmanagement.health.support;

import com.petmanagement.shared.domain.model.PageRequest;

import java.time.LocalDate;

public final class TestCommonMother {

    // === Dates ===

    private static final LocalDate TODAY = LocalDate.now();

    public static final LocalDate EARLY_DATE = LocalDate.of(TODAY.getYear() - 1, 1, 10);
    public static final LocalDate MIDDLE_DATE = LocalDate.of(TODAY.getYear() - 1, 7, 1);
    public static final LocalDate RECENT_DATE = TODAY.minusDays(7);
    public static final LocalDate FUTURE_DATE = TODAY.plusYears(1);

    // === Page Requests ===

    public static final PageRequest DEFAULT_PAGE_REQUEST = PageRequest.of(0, 10);
    public static final PageRequest FIRST_PAGE = PageRequest.of(0, 10);
    public static final PageRequest SINGLE_ITEM_PAGE = PageRequest.of(0, 1);
    public static final PageRequest SECOND_PAGE = PageRequest.of(1, 10);
    public static final PageRequest SMALL_PAGE = PageRequest.of(0, 5);
    public static final PageRequest LARGE_PAGE = PageRequest.of(0, 50);

    private TestCommonMother() {
    }

}
