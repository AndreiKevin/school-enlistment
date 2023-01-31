package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    static final Schedule DEFAULT_SCHEDULE = new Schedule(Days.MTH, Period.H1430);
    @Test
    void enlist_2_sections_no_conflict() {
        // Given 1 student and 2 sections with no conflict
        Student student = new Student(1);
        Room room = new Room("AGH20", 45);
        Section sec1 = new Section("A", new Schedule(Days.MTH, Period.H1000), room);
        Section sec2 = new Section("B", new Schedule(Days.TF, Period.H1430), room);

        // When the student enlists in both sections
        student.enlist(sec1);
        student.enlist(sec2);

        // Then we'll find both sections inside the student and
        // only those sections (no others)
        Collection<Section> sections = student.getSections();
        assertAll(
                () -> assertTrue(sections.containsAll(List.of(sec1, sec2))),
                () -> assertEquals(2, sections.size())
        );
    }

    @Test
    void enlist_2_sections_same_schedule() {
        // Given 1 student and 2 sections w/ same schedule
        Student student = new Student(1);
        Room room1 = new Room("AGH20", 45);
        Room room2 = new Room("HSS30", 45);
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, room1);
        Section sec2 = new Section("B", DEFAULT_SCHEDULE, room2);

        // When student enlist in both sections
        student.enlist(sec1);

        // Then at 2nd section an exception should be thrown
        assertThrows(ScheduleConflictException.class, () -> student.enlist(sec2));
    }
}

