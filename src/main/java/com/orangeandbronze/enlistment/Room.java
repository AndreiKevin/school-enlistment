package com.orangeandbronze.enlistment;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

import java.util.*;

class Room {
    private final String roomName;
    private final int capacity;
    private int currentStudentOccupied = 0;

    Room(String roomName, int capacity){
        notBlank(roomName);
        isTrue(isAlphanumeric(roomName), "roomName must be alphanumeric, was " + roomName);

        if (capacity <= 0 ) {
            throw new IllegalArgumentException("Room Capacity cannot be non-positive was: " + capacity);
        }
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public int currentCapacity() {
        return currentStudentOccupied;
    }

    public void addStudent(){
        checkIsFull();
        ++currentStudentOccupied;
    }

    public void removeStudent(){
        checkIsNotEmpty();
        --currentStudentOccupied;
    }

    private void checkIsFull(){
        if (currentStudentOccupied >= capacity) {
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
