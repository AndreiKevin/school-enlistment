package com.orangeandbronze.enlistment;

public class RemovingFromEmptyRoomException extends RuntimeException {
    public RemovingFromEmptyRoomException(String roomStr) {
        super("Current Students are 0. Cannot remove any more students in room " + roomStr);
    }
}
