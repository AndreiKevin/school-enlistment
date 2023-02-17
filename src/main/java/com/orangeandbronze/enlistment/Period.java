package com.orangeandbronze.enlistment;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Period {

    private final int start;
    private final int end;
    Period(int start, int end) {
        check30MinuteIncrement(start, end);
        checkValidDuration(start, end);
        checkEndBeforeStart(start, end);

        this.start = start;
        this.end = end;
    }

    boolean isConflictingPeriod(Period other) {
        int otherStart = other.start;
        int otherEnd = other.end;

        if(otherStart >= start && otherStart <= end) {
            return true;
        }
        return otherEnd >= start && otherEnd <= end;
    }

    private void check30MinuteIncrement(int start, int end) {
        int startHundreds = start % 100;
        int endHundreds = end % 100;
        int interval = Math.abs(startHundreds - endHundreds);
        if (interval != 30 && interval != 0) {
            throw new InvalidPeriodException("The time must be in increments of 30");
        }
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

