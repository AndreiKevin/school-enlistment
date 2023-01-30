package com.orangeandbronze.enlistment;

import java.util.Objects;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

class Section {
    private final String sectionId;
    private final Schedule schedule;

    private final Room room;

    Section(String sectionId, Schedule schedule, Room room) {
        notBlank(sectionId);
        isTrue(isAlphanumeric(sectionId), "sectionId must be alphanumeric, was " + sectionId);
        this.sectionId = sectionId;
        this.schedule = schedule;
        this.room = room;
    }

    boolean hasConflict(Section other) {
        return this.schedule.equals(other.schedule);
    }

    void checkForConflict(Section other) {
        if(this.schedule.equals(other.schedule)) {
            throw new ScheduleConflictException("current section " + this +
                    " has same schedule as new section " + other +
                    " at schedule " + this.schedule);
        }
    }

    void checkIfFull(){ if(!this.room.isNotFull()) {
        throw new SectionCapacityException("current section " + this +
                " is fully occupied " );
        }
    }

    void addStudent(){ room.addStudent(); }

    @Override
    public String toString() {
        return sectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        return this.sectionId == section.sectionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionId);
    }
}



