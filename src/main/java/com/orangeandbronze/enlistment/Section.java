package com.orangeandbronze.enlistment;

import java.util.Objects;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

class Section {
    private final String sectionId;
    private final Schedule schedule;
    private final Room room;
    private final Subject subject;

    private int currentStudentOccupied = 0;


    Section(String sectionId, Schedule schedule, Room room, Subject subject) {
        notBlank(sectionId);
        isTrue(isAlphanumeric(sectionId), "sectionId must be alphanumeric, was " + sectionId);

        notNull(schedule);
        notNull(room);
        notNull(subject);

        this.sectionId = sectionId;
        this.schedule = schedule;
        this.room = room;
        this.subject = subject;
    }

    void checkForConflict(Section other) {
        checkSameSchedule(other);
        checkSameSubject(other);
    }

    private void checkSameSchedule(Section other){
        if (hasScheduleConflict(other)) {
            throw new ScheduleConflictException("current section " + this +
                    " has same schedule as new section " + other +
                    " at schedule " + this.schedule);
        }
    }
    private void checkSameSubject(Section other){
        if (hasSameSubjectConflict(other)) {
            throw new SameSubjectException("current section " + this +
                    " has same subject as new section " + other +
                    " with subject " + this.subject);
        }
    }

    private void checkIfTakenPrerequisite(){

    }

    private boolean hasTakenPrerequisite(Student other){return false;}
    private boolean hasSameSubjectConflict(Section other){return this.subject.equals(other.subject);}
    private boolean hasScheduleConflict(Section other) {return this.schedule.equals(other.schedule);}


    int getCurrentStudentOccupied() {
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



