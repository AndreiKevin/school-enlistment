package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest
{
    static final Schedule DEFAULT_SCHEDULE = new Schedule(Days.MTH, Period.H1430);
    @Test
    void enlist_room_capacity_full(){
        //Given 2 students a section that has a room capacity of 1
        Student student1 = new Student(1);
        Student student2 = new Student(2);
        Room room = new Room("AGH20", 1);
        Section sec = new Section("A", DEFAULT_SCHEDULE, room);

        //When student 1 enlists
        student1.enlist(sec);

        //Then an exception should be thrown when student 2 enlists
        assertThrows(SectionCapacityException.class, () -> student2.enlist(sec));
    }

    @Test
    void enlist_room_capacity_not_full(){
        //Given 2 students a section that has a room capacity of 2
        Student student1 = new Student(1);
        Student student2 = new Student(2);
        Room room = new Room("AGH20", 2);
        Section sec = new Section("A", DEFAULT_SCHEDULE, room);

        //When student 1 enlists
        student1.enlist(sec);

        //Then student 2 should also be able to enlist
        assertDoesNotThrow(() -> student2.enlist(sec));
    }
}
