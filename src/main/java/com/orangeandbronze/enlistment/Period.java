package com.orangeandbronze.enlistment;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Period {

    private final int start;
    private final int end;
    Period(int start, int end) {
        checkValidDuration(start, end);
        checkEndBeforeStart(start, end);
        // 830 - 1730
        this.start = start;
        this.end = end;
    }
    private void checkValidDuration(int start, int end) {
        if(start < 830 || start > 1730 || end < 830 || end > 1730)
            throw new InvalidPeriodException("The start or end is before 830 or after 1730");
    }

    private void checkEndBeforeStart(int start, int end) {
        if(start >= end) {
            throw new InvalidPeriodException("End period is at the start or before the start of the period");
        }
    }

    @Override
    public String toString() {
        return "Period{" +
                "start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Period period = (Period) o;

        return new EqualsBuilder().append(start, period.start).append(end, period.end).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(start).append(end).toHashCode();
    }
}

