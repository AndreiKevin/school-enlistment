package com.orangeandbronze.enlistment;

import java.math.BigDecimal;
import java.util.*;
import static org.apache.commons.lang3.Validate.*;


class Student {
    private final int studentNumber;
    private final Collection<Section> enrolledSections = new HashSet<>();
    private final Collection<Subject> takenSubjects = new HashSet<>();
    private final DegreeProgram degreeProgram;

    Student(int studentNumber, DegreeProgram degreeProgram, Collection<Section> enrolledSections, Collection<Subject> takenSubjects) {
        this.studentNumber = studentNumber;
        this.degreeProgram = degreeProgram;

        this.enrolledSections.addAll(enrolledSections);
        this.enrolledSections.forEach(currSection -> currSection.addStudent());

        this.takenSubjects.addAll(takenSubjects);
    }

    void enlist(Section newSection) {
        notNull(newSection);
        enrolledSections.forEach(currSection -> currSection.checkForConflict(newSection));
        newSection.checkIfPrerequisitesTaken(takenSubjects);

        enrolledSections.add(newSection);
        newSection.addStudent();
    }

    void cancel(Section section){
        notNull(section);
        if(!enrolledSections.contains(section)) {
            throw new CancelNotEnlistedSectionException("Cannot cancel enlistment because student is not enlisted in section. student was "
                    + this + ", section was " + section);
        }

        enrolledSections.remove(section);
        section.removeStudent();
    }

    BigDecimal requestAssessment(){
        return AssessmentHandler.assess(enrolledSections);
    }

    Collection<Section> getEnrolledSections() {
        return new ArrayList<>(enrolledSections);
    }

    @Override
    public String toString() {
        return "Student#" + studentNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentNumber == student.studentNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentNumber);
    }
}
