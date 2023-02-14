package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
public class PeriodTest {
    private static final Period DEFAULT_PERIOD = new Period(830, 1100);
    private static final Schedule DEFAULT_SCHEDULE = new Schedule(Days.MTH, DEFAULT_PERIOD);
    private static final Subject DEFAULT_SUBJECT_A = new Subject("A", 1);
    private static final Subject DEFAULT_SUBJECT_B = new Subject("B", 1);

}