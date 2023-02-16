package com.orangeandbronze.enlistment;

import java.util.Objects;

import static org.apache.commons.lang3.Validate.notNull;

class Schedule {
    private final Days days;
    private final Period period;

    Schedule(Days days, Period period) {
        notNull(days);
        notNull(period);

        this.days = days;
        this.period = period;
    }

    boolean isConflicting(Schedule other) {
        return this.period.isConflictingPeriod(other.period);
    }

    public Period getPeriod() {
        return period;
    }

    @Override
    public String toString() {
        return days + " " + period;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return days == schedule.days && period == schedule.period;
    }

    @Override
    public int hashCode() {
        return Objects.hash(days, period);
    }
}

enum Days {
    MTH, TF, WS
}

