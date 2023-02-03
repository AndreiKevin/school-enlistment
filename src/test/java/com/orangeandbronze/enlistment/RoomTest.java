package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest
{
    static final Schedule DEFAULT_SCHEDULE = new Schedule(Days.MTH, Period.H1430);
    @Test
    void enlist_room_capacity_full(){
        //Given 2 students  and a section that has a room capacity of 1
        Student student1 = new Student(1);
        Student student2 = new Student(2);
        Room room = new Room("AGH20", 1);
        Section sec = new Section("A", DEFAULT_SCHEDULE, room);

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
        Section sec = new Section("A", DEFAULT_SCHEDULE, room);

        //When student 1 enlists
        student1.enlist(sec);

        //Then student 2 should also be able to enlist
        assertDoesNotThrow(() -> student2.enlist(sec));
    }

    @Test
    void cancel_does_not_belong_to_class(){
        //Given a student and 2 sections
        Student student = new Student(1);
        Room room1 = new Room("AGH20", 10);
        Room room2 = new Room("AGH35", 10);

        Section section1 = new Section("A", DEFAULT_SCHEDULE, room1);
        Section section2 = new Section("B", DEFAULT_SCHEDULE, room2);

        //When student 1 enlists in section 1
        student.enlist(section1);

        //Then throw an exception when student enrolls in section 2 since they are not enrolled there
        assertThrows(CancelNotEnlistedSectionException.class, () -> student.cancel((section2)));
    }


    @Test
    void cancel_belongs_to_class(){
        //Given a student and a section
        Student student = new Student(1);
        Room room = new Room("AGH20", 10);
        Section section = new Section("S12", DEFAULT_SCHEDULE, room);

        //When student enlists in section
        student.enlist(section);

        //And student cancels enlistment
        student.cancel(section);

        //They should not be a part of any section
        assertEquals(0, student.getSections().size());
    }

    @Test
    void cancel_enlisment_should_decrement() {
        //Given a student, a room, and a section
        Student student1 = new Student(1);
        Room room = new Room("AGH20", 1);
        Section section = new Section("A", DEFAULT_SCHEDULE, room);

        //When student cancels enlistment to a section
        student1.enlist(section);
        student1.cancel(section);

        //Then remove from student's section list and decrement capacity
        assertAll(
                () -> assertFalse(student1.getSections().contains(section)),
                () -> assertEquals(0, section.getCurrentStudentOccupied())
        );
    }
}
