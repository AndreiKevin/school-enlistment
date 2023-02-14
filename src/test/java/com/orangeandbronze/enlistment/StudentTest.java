package com.orangeandbronze.enlistment;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    private static final Schedule DEFAULT_SCHEDULE = new Schedule(Days.MTH, Period.H1430);
    private static final Subject DEFAULT_SUBJECT_A = new Subject("A", 1);
    private static final Subject DEFAULT_SUBJECT_B = new Subject("B", 1);
    private static final Subject DEFAULT_SUBJECT_C = new Subject("C", 3, Arrays.asList(DEFAULT_SUBJECT_A));
    private static final Subject DEFAULT_SUBJECT_D = new Subject("D", 3);
    private static final LaboratorySubject DEFAULT_LAB_SUBJECT_A = new LaboratorySubject("A", 1, Collections.emptyList());

    private static final DegreeProgram DEFAULT_DEGREE_PROGRAM = new DegreeProgram(Arrays.asList(DEFAULT_SUBJECT_A, DEFAULT_SUBJECT_B,
            DEFAULT_LAB_SUBJECT_A, DEFAULT_SUBJECT_C));

    private static final Room DEFAULT_ROOM = new Room("AGH20", 45);

    @Test
    void enlist_2_sections_no_conflict() {
        // Given 1 student and 2 sections with no conflict
        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .getResult();
        Section sec1 = new Section("A", new Schedule(Days.MTH, Period.H1000), DEFAULT_ROOM, DEFAULT_SUBJECT_A);
        Section sec2 = new Section("B", new Schedule(Days.TF, Period.H1430), DEFAULT_ROOM, DEFAULT_SUBJECT_B);

        // When the student enlists in both sections
        student.enlist(sec1);
        student.enlist(sec2);

        // Then we'll find both sections inside the student and
        // only those sections (no others)
        Collection<Section> sections = student.getEnlistedSections();
        assertAll(
                () -> assertTrue(sections.containsAll(List.of(sec1, sec2))),
                () -> assertEquals(2, sections.size())
        );
    }

    @Test
    void enlist_2_sections_same_schedule() {
        // Given 1 student and 2 sections w/ same schedule
        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .getResult();
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
        Student student1 = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .getResult();
        Student student2 = (new StudentBuilder())
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .setStudentNumber(2)
                .getResult();

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
        Student student1 = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .getResult();
        Student student2 = (new StudentBuilder())
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .setStudentNumber(2)
                .getResult();
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

        Section section1 = new Section("A", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_A);
        Section section2 = new Section("B", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_A);

        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .setEnlistedSections(Arrays.asList(section1))
                .getResult();

        //When student 1 is enlisted in section 1, but tries to cancel from section 2.

        //Then throw an exception when student enrolls in section 2 since they are not enrolled there
        assertThrows(CancelNotEnlistedSectionException.class, () -> student.cancel((section2)));
    }


    @Test
    void cancel_belongs_to_class(){
        //Given a section and a student
        Section section = new Section("S12", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_A);

        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .setEnlistedSections(Arrays.asList(section))
                .getResult();

        //When student cancels enlistment
        student.cancel(section);

        //They should not be a part of any section
        assertEquals(0, student.getEnlistedSections().size());
    }

    @Test
    void cancel_enlistment_should_decrement() {
        //Given a room, a section and a student
        Room room = new Room("AGH20", 1);
        Section section = new Section("A", DEFAULT_SCHEDULE, room, DEFAULT_SUBJECT_A);

        //When student cancels enlistment to a section
        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .setEnlistedSections(Arrays.asList(section))
                .getResult();
        student.cancel(section);

        //Then remove from student's section list and decrement capacity
        assertAll(
                () -> assertFalse(student.getEnlistedSections().contains(section)),
                () -> assertEquals(0, section.getCurrentStudentOccupied())
        );
    }

    @Test
    void enlist_2_sections_same_subjects_conflict(){
        // Given 1 student and 2 sections w/ same schedule
        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .getResult();
        Section sec1 = new Section("A", new Schedule(Days.MTH, Period.H1000), DEFAULT_ROOM, DEFAULT_SUBJECT_A);
        Section sec2 = new Section("B", new Schedule(Days.TF, Period.H1430), DEFAULT_ROOM, DEFAULT_SUBJECT_A);

        // When student enlist in both sections
        student.enlist(sec1);

        // Then at 2nd section an exception should be thrown
        assertThrows(SameSubjectException.class, () -> student.enlist(sec2));
    }

    @Test
    void enlist_section_with_missing_prerequisite(){
        // Given 1 student and 1 section w/ prerequisite
        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .getResult();

        Section sec = new Section("A", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_C);

        // When student enlist in section with missing prerequisite
        // Then an exception should be thrown
        assertThrows(MissingPrerequisiteException.class, () -> student.enlist(sec));
    }

    @Test
    void enlist_section_with_prerequisite(){
        // Given 1 student and 1 section w/ prerequisite
        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(DEFAULT_DEGREE_PROGRAM)
                .setTakenSubjects(Arrays.asList(DEFAULT_SUBJECT_A))
                .getResult();
        Section sec = new Section("A", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_C);

        // When student enlist in section with missing prerequisite
        student.enlist(sec);

        // Then an exception should be thrown
        assertEquals(student.getEnlistedSections().size(), 1);
    }

    @Test
    void get_assessment_for_units_without_lab(){
        // Given a student with 3 non-laboratory sections of 5 units total

        Section section1 = new Section("A", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_A);
        Section section2 = new Section("B", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_B);
        Section section3 = new Section("C", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_C);

        List<Section> sections=  Arrays.asList(section1, section2, section3);

        // Assessment of fees should return 14560
        // 1.12((2000 * 5) + 3000) == 14560
        BigDecimal totalFees = AssessmentHandler.assess(sections);
        assertTrue(BigDecimal.valueOf(14560).compareTo(totalFees) == 0);
    }

    @Test
    void get_assessment_for_units_with_lab(){
        // Given a student with 2 non-laboratory sections and 1 laboratory subject of 3 units total
        Section section1 = new Section("A", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_A);
        Section section2 = new Section("B", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_B);
        Section lab_section1 = new Section("C", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_LAB_SUBJECT_A);

        List<Section> sections = Arrays.asList(section1, section2, lab_section1);

        // Assessment of fees should return 11200
        // 1.12((2000 * 3) + 1000 + 3000) == 11200
        BigDecimal totalFees = AssessmentHandler.assess(sections);
        assertTrue(BigDecimal.valueOf(11200).compareTo(totalFees) == 0);
    }

    @Test
    void enroll_to_section_not_in_degree_program() {
        // Given a student, and a section for a subject not in their degree program
        DegreeProgram degreeProgram = new DegreeProgram(Arrays.asList(DEFAULT_SUBJECT_A, DEFAULT_SUBJECT_B, DEFAULT_SUBJECT_C));

        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(degreeProgram)
                .getResult();
        Section section = new Section("D", DEFAULT_SCHEDULE, DEFAULT_ROOM, DEFAULT_SUBJECT_D);

        // WHen the student enlists
        // An error is thrown
        assertThrows(NotInDegreeProgramException.class, () -> student.enlist(section));
    }

    @Test
    void student_cannot_enroll_more_than_24_units() {
        Subject overloadedSubject = new Subject("SUBJECT1", 25);
        Student student = (new StudentBuilder())
                .setStudentNumber(1)
                .setDegreeProgram(new DegreeProgram(Arrays.asList(overloadedSubject)))
                .getResult();
        Section section = new Section("E", DEFAULT_SCHEDULE, DEFAULT_ROOM, overloadedSubject);

        assertAll(
                () -> assertThrows(OverloadedUnitsException.class, () -> student.enlist(section)),
                () -> assertEquals(student.getEnlistedSections().size(), 0)
        );
    }

}

