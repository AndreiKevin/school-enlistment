package com.orangeandbronze.enlistment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

class Subject {
    protected final String subjectId;
    protected final Collection<Subject> prerequisites = new HashSet<>();
    protected final int units;

    Subject(String subjectId, int units, Collection<Subject> prerequisites){
        notBlank(subjectId);
        isTrue(isAlphanumeric(subjectId), "subjectId must be alphanumeric, was " + subjectId);

        if (units <= 0 ) {
            throw new IllegalArgumentException("Units cannot be non-positive was: " + units);
        }

        if (prerequisites == null) {
            throw new NullPointerException("prerequisites should not be null");
        }

        this.subjectId = subjectId;
        this.units = units;
        this.prerequisites.addAll(prerequisites);
    }

    public Subject(String subjectId, int units){
        this(subjectId, units, Collections.emptyList());
    }

    boolean isPrerequisitesTaken(Collection<Subject> takenSubjects) {
        return takenSubjects.containsAll(prerequisites);
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
