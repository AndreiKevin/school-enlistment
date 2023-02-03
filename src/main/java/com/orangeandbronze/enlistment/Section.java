package com.orangeandbronze.enlistment;

import java.util.Objects;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

class Section {
    private final String sectionId;
    private final Schedule schedule;
    private final Room room;

    private int currentStudentOccupied = 0;


    Section(String sectionId, Schedule schedule, Room room) {
        notBlank(sectionId);
        isTrue(isAlphanumeric(sectionId), "sectionId must be alphanumeric, was " + sectionId);

        notNull(schedule);
        notNull(room);

        this.sectionId = sectionId;
        this.schedule = schedule;
        this.room = room;
    }

    void checkForConflict(Section other) {
        if (hasConflict(other)) {
            throw new ScheduleConflictException("current section " + this +
                    " has same schedule as new section " + other +
                    " at schedule " + this.schedule);
        }
    }

    private boolean hasConflict(Section other) {
        return this.schedule.equals(other.schedule);
    }


    int currentCapacity() {
        return currentStudentOccupied;
    }

    void addStudent(){
        checkIsFull();
        ++currentStudentOccupied;
    }

    void removeStudent(){
        checkIsNotEmpty();
        --currentStudentOccupied;
    }

    private void checkIsFull(){
        if (currentStudentOccupied >= room.capacity) {
            throw new RoomCapacityException("current section " + this +
                    " is fully occupied ");
        }
    }
    private void checkIsNotEmpty() {
        if (currentStudentOccupied == 0) {
            throw new RemovingFromEmptyRoomException("Current student count is 0. Cannot remove any more students in room "
                    + this);
        }
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

        return this.sectionId.equals(section.sectionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sectionId);
    }
}



