package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    static final Schedule DEFAULT_SCHEDULE = new Schedule(Days.MTH, Period.H1430);
    static final Subject DEFAULT_SUBJECT_A = new Subject("A", 1);
    static final Subject DEFAULT_SUBJECT_B = new Subject("B", 1);
    @Test
    void enlist_2_sections_no_conflict() {
        // Given 1 student and 2 sections with no conflict
        Student student = new Student(1);
        Room room = new Room("AGH20", 45);
        Section sec1 = new Section("A", new Schedule(Days.MTH, Period.H1000), room, DEFAULT_SUBJECT_A);
        Section sec2 = new Section("B", new Schedule(Days.TF, Period.H1430), room, DEFAULT_SUBJECT_B);

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
        Section sec1 = new Section("A", DEFAULT_SCHEDULE, room1, DEFAULT_SUBJECT_A);
        Section sec2 = new Section("B", DEFAULT_SCHEDULE, room2, DEFAULT_SUBJECT_B);

        // When student enlist in both sections
        student.enlist(sec1);

        // Then at 2nd section an exception should be thrown
        assertThrows(ScheduleConflictException.class, () -> student.enlist(sec2));
    }

    @Test
    void enlist_room_capacity_full(){
        //Given 2 students  and a section that has a room capacity of 1
        Student student1 = new Student(1);
        Student student2 = new Student(2);
        Room room = new Room("AGH20", 1);
        Section sec = new Section("A", DEFAULT_SCHEDULE, room, DEFAULT_SUBJECT_A);

        //When student 1 enlists
        student1.enlist(sec);

        //Then an exception should be thrown when student 2 enlists
        assertThrows(RoomCapacityException.class, () -> student2.enlist(sec));
    }

    @Test
    void enlist_room_capacity_not_full(){
        //Given 2 students and a section that has a room capacity of 2
        Student student1 = new Student(1);
        Student student2 = new Student(2);
        Room room = new Room("AGH20", 2);
        Section sec = new Section("A", DEFAULT_SCHEDULE, room, DEFAULT_SUBJECT_A);

        //When student 1 enlists
        student1.enlist(sec);

        //Then student 2 should also be able to enlist
        assertDoesNotThrow(() -> student2.enlist(sec));
    }

    @Test
    void cancel_does_not_belong_to_class(){
        //Given 2 sections and a student enlisted in one of the sections
        Room room1 = new Room("AGH20", 10);
        Room room2 = new Room("AGH35", 10);

        Section section1 = new Section("A", DEFAULT_SCHEDULE, room1, DEFAULT_SUBJECT_A);
        Section section2 = new Section("B", DEFAULT_SCHEDULE, room2, DEFAULT_SUBJECT_A);

        Student student = new Student(1, Arrays.asList(section1));

        //When student 1 is enlisted in section 1, but tries to cancel from section 2.

        //Then throw an exception when student enrolls in section 2 since they are not enrolled there
        assertThrows(CancelNotEnlistedSectionException.class, () -> student.cancel((section2)));
    }


    @Test
    void cancel_belongs_to_class(){
        //Given a section and a student
        Room room = new Room("AGH20", 10);
        Section section = new Section("S12", DEFAULT_SCHEDULE, room, DEFAULT_SUBJECT_A);

        Student student = new Student(1, Arrays.asList(section));

        //When student cancels enlistment
        student.cancel(section);

        //They should not be a part of any section
        assertEquals(0, student.getSections().size());
    }

    @Test
    void cancel_enlistment_should_decrement() {
        //Given a room, a section and a student
        Room room = new Room("AGH20", 1);
        Section section = new Section("A", DEFAULT_SCHEDULE, room, DEFAULT_SUBJECT_A);

        //When student cancels enlistment to a section
        Student student1 = new Student(1, Arrays.asList(section));
        student1.cancel(section);

        //Then remove from student's section list and decrement capacity
        assertAll(
                () -> assertFalse(student1.getSections().contains(section)),
                () -> assertEquals(0, section.getCurrentStudentOccupied())
        );
    }
}

