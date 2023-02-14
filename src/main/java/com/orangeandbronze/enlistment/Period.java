package com.orangeandbronze.enlistment;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Period {

    private final int start;
    private final int end;
    Period(int start, int end) {
        // 830 - 1730
        this.start = start;
        this.end = end;
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

