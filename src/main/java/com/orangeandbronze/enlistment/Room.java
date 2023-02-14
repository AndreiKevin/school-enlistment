package com.orangeandbronze.enlistment;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

import java.util.*;

class Room {
    private Collection<Section> heldSections = new HashSet<>();
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

    Room(String roomName, int capacity, Collection<Section> heldSections){
        this(roomName, capacity);
        this.heldSections.addAll(heldSections);
    }

    Collection<Section> getHeldSections(){
        return new ArrayList<Section>(heldSections);
    }

    void addSection(Section newSection){
        notNull(newSection);
        heldSections.add(newSection);
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
