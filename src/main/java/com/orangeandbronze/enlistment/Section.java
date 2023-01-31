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

    public void checkForConflict(Section other) {
        if(hasConflict(other)) {
            throw new ScheduleConflictException("current section " + this +
                    " has same schedule as new section " + other +
                    " at schedule " + this.schedule);
        }
    }

    public void addStudent(){
        room.addStudent();
    }

    public void removeStudent(){
        room.removeStudent();
    }


    private boolean hasConflict(Section other) {
        return this.schedule.equals(other.schedule);
    }

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



