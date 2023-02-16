package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
public class SectionTest {
    private static final Schedule DEFAULT_SCHEDULE = new Schedule(Days.MTH, new Period(830, 1030));
    private static final Subject DEFAULT_SUBJECT_A = new Subject("A", 1);
    private static final Subject DEFAULT_SUBJECT_B = new Subject("B", 1);
    @Test
    void sections_cannot_have_same_room_and_schedule() {
        // Given a section with a room and schedule
        Room room = new Room("AGH20", 45);
        Section section = new Section("1", DEFAULT_SCHEDULE, room, DEFAULT_SUBJECT_A);

        // When a new section with the same room and schedule is made
        // Then throw an error
        assertThrows(SectionsWithSameRoomAndScheduleException.class, () -> new Section("2", DEFAULT_SCHEDULE, room, DEFAULT_SUBJECT_B));
    }
}
