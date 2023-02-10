package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    static final Schedule DEFAULT_SCHEDULE = new Schedule(Days.MTH, Period.H1430);
    static final Subject DEFAULT_SUBJECT_A = new Subject("A", 1);
    static final Subject DEFAULT_SUBJECT_B = new Subject("B", 1);
    static final Subject DEFAULT_SUBJECT_C = new Subject("C", 3, Arrays.asList(DEFAULT_SUBJECT_A));
    static final LaboratorySubject DEFAULT_LAB_SUBJECT_A = new LaboratorySubject("A", 1, Collections.emptyList());

    static final DegreeProgram DEFAULT_DEGREE_PROGRAM = new DegreeProgram(Arrays.asList(DEFAULT_SUBJECT_A, DEFAULT_SUBJECT_B,
            DEFAULT_LAB_SUBJECT_A, DEFAULT_SUBJECT_C));

    @Test
    void enlist_2_sections_no_conflict() {
        // Given 1 student and 2 sections with no conflict
        Student student = new Student(1, DEFAULT_DEGREE_PROGRAM);
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
        Student student = new Student(1, DEFAULT_DEGREE_PROGRAM);
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
        Student student1 = new Student(1, DEFAULT_DEGREE_PROGRAM);
        Student student2 = new Student(2, DEFAULT_DEGREE_PROGRAM);
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
        Student student1 = new Student(1, DEFAULT_DEGREE_PROGRAM);
        Student student2 = new Student(2, DEFAULT_DEGREE_PROGRAM);
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

        Student student = new Student(1, DEFAULT_DEGREE_PROGRAM, Arrays.asList(section1));

        //When student 1 is enlisted in section 1, but tries to cancel from section 2.

        //Then throw an exception when student enrolls in section 2 since they are not enrolled there
        assertThrows(CancelNotEnlistedSectionException.class, () -> student.cancel((section2)));
    }


    @Test
    void cancel_belongs_to_class(){
        //Given a section and a student
        Room room = new Room("AGH20", 10);
        Section section = new Section("S12", DEFAULT_SCHEDULE, room, DEFAULT_SUBJECT_A);

        Student student = new Student(1, DEFAULT_DEGREE_PROGRAM, Arrays.asList(section));

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
        Student student1 = new Student(1, DEFAULT_DEGREE_PROGRAM, Arrays.asList(section));
        student1.cancel(section);

        //Then remove from student's section list and decrement capacity
        assertAll(
                () -> assertFalse(student1.getSections().contains(section)),
                () -> assertEquals(0, section.getCurrentStudentOccupied())
        );
    }

    @Test
    void enlist_2_sections_same_subjects_conflict(){
        // Given 1 student and 2 sections w/ same schedule
        Student student = new Student(1, DEFAULT_DEGREE_PROGRAM);
        Room room1 = new Room("AGH20", 45);
        Room room2 = new Room("HSS30", 45);
        Section sec1 = new Section("A", new Schedule(Days.MTH, Period.H1000), room1, DEFAULT_SUBJECT_A);
        Section sec2 = new Section("B", new Schedule(Days.TF, Period.H1430), room2, DEFAULT_SUBJECT_A);

        // When student enlist in both sections
        student.enlist(sec1);

        // Then at 2nd section an exception should be thrown
        assertThrows(SameSubjectException.class, () -> student.enlist(sec2));
    }

    @Test
    void enlist_section_with_missing_prerequisite(){
        // Given 1 student and 1 section w/ prerequisite
        Student student = new Student(1, DEFAULT_DEGREE_PROGRAM);
        Room room = new Room("AGH20", 45);
        Section sec = new Section("A", new Schedule(Days.MTH, Period.H1000), room, DEFAULT_SUBJECT_C);

        // When student enlist in section with missing prerequisite
        // Then an exception should be thrown
        assertThrows(MissingPrerequisiteException.class, () -> student.enlist(sec));
    }

    @Test
    void enlist_section_with_prerequisite(){
        // Given 1 student and 1 section w/ prerequisite
        Student student = new Student(1, DEFAULT_DEGREE_PROGRAM,Collections.emptyList(), Arrays.asList(DEFAULT_SUBJECT_A));
        Room room = new Room("AGH20", 45);
        Section sec = new Section("A", new Schedule(Days.MTH, Period.H1000), room, DEFAULT_SUBJECT_C);

        // When student enlist in section with missing prerequisite
        student.enlist(sec);

        // Then an exception should be thrown
        assertEquals(student.getSections().size(), 1);
    }

    @Test
    void get_assessment_for_units_without_lab(){
        // Given a student with 3 non-laboratory sections of 5 units total
        Room room = new Room("AGH20", 10);

        Section section1 = new Section("A", new Schedule(Days.MTH, Period.H1000), room, DEFAULT_SUBJECT_A);
        Section section2 = new Section("B", new Schedule(Days.MTH, Period.H1130), room, DEFAULT_SUBJECT_B);
        Section section3 = new Section("C", new Schedule(Days.MTH, Period.H1300), room, DEFAULT_SUBJECT_C);

        Student student = new Student(1, DEFAULT_DEGREE_PROGRAM,Arrays.asList(section1, section2, section3));

        // Assessment of fees should return 14560
        // 1.12((2000 * 5) + 3000) == 14560
        BigDecimal totalFees = student.requestAssessment();
        assertTrue(BigDecimal.valueOf(14560).compareTo(totalFees) == 0);
    }

    @Test
    void get_assessment_for_units_with_lab(){
        // Given a student with 2 non-laboratory sections and 1 laboratory subject of 3 units total
        Room room = new Room("AGH20", 10);

        Section section1 = new Section("A", new Schedule(Days.MTH, Period.H1000), room, DEFAULT_SUBJECT_A);
        Section section2 = new Section("B", new Schedule(Days.MTH, Period.H1130), room, DEFAULT_SUBJECT_B);
        Section lab_section1 = new Section("C", new Schedule(Days.MTH, Period.H1300), room, DEFAULT_LAB_SUBJECT_A);

        Student student = new Student(1, DEFAULT_DEGREE_PROGRAM,Arrays.asList(section1, section2, lab_section1));

        // Assessment of fees should return 11200
        // 1.12((2000 * 3) + 1000 + 3000) == 11200
        BigDecimal totalFees = student.requestAssessment();
        assertTrue(BigDecimal.valueOf(11200).compareTo(totalFees) == 0);
    }

}

