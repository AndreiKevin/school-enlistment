package com.orangeandbronze.enlistment;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

import java.util.*;

class Room {
    private final Collection<Schedule> heldSchedule = new HashSet<>();
    private final String roomName;
    protected final int capacity;

    Room(String roomName, int capacity){
        notBlank(roomName);
        isTrue(isAlphanumeric(roomName), "roomName must be alphanumeric, was " + roomName);

        if (capacity <= 0 ) {
            throw new IllegalArgumentException("Room Capacity cannot be non-positive was: " + capacity);
        }
        this.roomName = roomName;
        this.capacity = capacity;
    }

    void tryAddSchedule(Schedule schedule){
        notNull(schedule);
        checkForConflicts(schedule);
        heldSchedule.add(schedule);
    }

    void checkForConflicts(Schedule newSchedule){
        heldSchedule.forEach(schedule -> {
            if(schedule.isConflicting(newSchedule)){
                throw new SectionsWithSameRoomAndScheduleException("Sections cannot have the same schedule. " +
                        "Schedules were" + newSchedule + " and " + schedule);
            }
        });
    }

    @Override
    public String toString() {
        return roomName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return Objects.equals(roomName, room.roomName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomName);
    }
}
