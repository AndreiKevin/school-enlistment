package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class PeriodTest {
    private static final Period DEFAULT_PERIOD = new Period(830, 1100);
    private static final Schedule DEFAULT_SCHEDULE = new Schedule(Days.MTH, DEFAULT_PERIOD);
    private static final Subject DEFAULT_SUBJECT_A = new Subject("A", 1);
    private static final Subject DEFAULT_SUBJECT_B = new Subject("B", 1);
    @Test
    void end_before_start_time_throws_exception() {
        // Given nothing
        // When initializing a period whose end time is before its start time
        // Then throw an error
        assertAll(
                () -> assertThrows(InvalidPeriodException.class, () -> new Period(1030, 930)),
                () -> assertThrows(InvalidPeriodException.class, () -> new Period(1030, 1030))
        );

    }
    @Test
    void period_within_830_1730() {
        // When initializing a period whose time is not within 830 1730
        // Then throw an error
        assertAll(
                () -> assertThrows(InvalidPeriodException.class, () -> new Period(330, 930)),
                () -> assertThrows(InvalidPeriodException.class, () -> new Period(1830, 930)),
                () -> assertThrows(InvalidPeriodException.class, () -> new Period(830, 330)),
                () -> assertThrows(InvalidPeriodException.class, () -> new Period(830, 1830))
        );
    }

    @Test
    void period_within_30_min_increments() {
        assertAll(
                () -> assertThrows(InvalidPeriodException.class, () -> new Period(875, 1000)),
                () -> assertThrows(InvalidPeriodException.class, () -> new Period(830, 1045))
        );
    }




}