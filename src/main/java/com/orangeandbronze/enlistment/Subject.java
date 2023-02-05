package com.orangeandbronze.enlistment;

import java.util.Objects;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

class Subject {
    private final String subjectId;
    private final int units;

    public Subject(String subjectId, int units){
        notBlank(subjectId);
        isTrue(isAlphanumeric(subjectId), "subjectId must be alphanumeric, was " + subjectId);

        if (units <= 0 ) {
            throw new IllegalArgumentException("Units cannot be non-positive was: " + units);
        }

        this.subjectId = subjectId;
        this.units = units;
    }

    @Override
    public String toString() {
        return subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(subjectId, subject.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjectId);
    }
}
