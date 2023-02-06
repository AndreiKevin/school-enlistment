package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
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

    @Test
    void enlist_same_subject_throws_error() {
        // Given 1 student enrolled in section A with a Math subject and section B with Math subject
        Student student = new Student(1,
                        new ArrayList<Section>(new Section("A", new Schedule(Days.TF, Period.H1430),
                        new Room("BestRoomEver", 40), new Subject("Math"))));
        Section sameSubjectSection = new Section("B", DEFAULT_SCHEDULE, new Room("DifferentRoom",
                          20, new Subject("Math")));

        // When student tries to enlist to B

        // Then throw an exception
        assertThrows(SameSubjectException.class, () -> student.enlist(sameSubjectSection));
    }

    @Test
    void enlist_subject_without_accomplished_prereq_throws_error() {
        // Given a student and a section A with a prerequisite subject required
        Student student = new Student(1);
        Section section = new Section("A", DEFAULT_SCHEDULE, new Room("Z", 10),
                new Subject("CCPROG2"), new Subject("CCPROG1")); // enroll to (subject) with (prereq)
        // When student tries to enlist in A
        // Then an error is thrown
        assertThrows(IncompletePrerequisiteException.class, () -> student.enlist(section));
    }

}

