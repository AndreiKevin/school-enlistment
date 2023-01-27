package com.orangeandbronze.enlistment;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

import java.util.*;

public class Room {
    private final String roomName;
    private final int capacity;
    private int currentStudentOccupied = 0;

    Room(String roomName, int capacity){
        notBlank(roomName);
        isTrue(isAlphanumeric(roomName), "roomName must be alphanumeric, was " + roomName);

        if (capacity <= 0 ) {
            throw new IllegalArgumentException("Room Capacity cannot be non-negative was: " + capacity);
        }
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public boolean isNotFullyOccupied(){
        return currentStudentOccupied < capacity;
    }

    public void addStudent(){
        ++currentStudentOccupied;
    }

    public void removeStudent(){
        if (currentStudentOccupied == 0){
            // TODO: Make an exception class for this.
            throw new RuntimeException("Current Students are 0. Cannot remove any more students.");
        }
        --currentStudentOccupied;
    }

}
