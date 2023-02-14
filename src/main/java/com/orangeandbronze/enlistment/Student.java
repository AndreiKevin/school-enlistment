package com.orangeandbronze.enlistment;

import java.math.BigDecimal;
import java.util.*;
import static org.apache.commons.lang3.Validate.*;


class Student {
    private final int studentNumber;
    private final Collection<Section> enlistedSections = new HashSet<>();
    private final Collection<Subject> takenSubjects = new HashSet<>();
    private final DegreeProgram degreeProgram;
    private int numberOfUnits;
    static final int UNIT_LIMIT = 24;

    Student(int studentNumber, DegreeProgram degreeProgram, Collection<Section> enlistedSections, Collection<Subject> takenSubjects) {
        this.studentNumber = studentNumber;
        this.degreeProgram = degreeProgram;

        this.enlistedSections.addAll(enlistedSections);
        this.enlistedSections.forEach(currSection -> currSection.addStudent());

        this.takenSubjects.addAll(takenSubjects);
        this.numberOfUnits = 0;
    }

    void enlist(Section newSection) {
        int newNumberOfUnits = computeNewNumberOfUnits(newSection);

        notNull(newSection);
        enlistedSections.forEach(currSection -> currSection.checkForConflict(newSection));

        Subject subject = newSection.getSubject();
        this.checkIfCanTakeSubject(subject);

        checkIfOverloadedUnits(newNumberOfUnits);

        enlistedSections.add(newSection);
        newSection.addStudent();

        this.numberOfUnits = newNumberOfUnits;
    }

    private void checkIfOverloadedUnits(int newNumberOfUnits){
        if(newNumberOfUnits > UNIT_LIMIT){
            throw new OverloadedUnitsException("Student has enlisted beyond the max capacity.");
        }
    }

    private void checkIfCanTakeSubject(Subject subject){
        this.checkIfPrerequisitesTaken(subject);
        this.checkIfInDegreeProgram(subject);
    }
    private void checkIfPrerequisitesTaken(Subject subject) {
        if(! subject.isPrerequisitesTaken(takenSubjects))
            throw new MissingPrerequisiteException("Not all prerequisites have been taken");
    }

    private void checkIfInDegreeProgram(Subject subject){
        degreeProgram.checkIfHasSubject(subject);
    }

    private int computeNewNumberOfUnits(Section newSection){ return this.numberOfUnits + newSection.getSubjectUnits(); }

    void cancel(Section section){
        notNull(section);
        if(!enlistedSections.contains(section)) {
            throw new CancelNotEnlistedSectionException("Cannot cancel enlistment because student is not enlisted in section. student was "
                    + this + ", section was " + section);
        }

        enlistedSections.remove(section);
        this.numberOfUnits -= section.getSubjectUnits();
        section.removeStudent();
    }

    BigDecimal requestAssessment(){
        return AssessmentHandler.assess(enlistedSections);
    }

    Collection<Section> getEnlistedSections() {
        return new ArrayList<>(enlistedSections);
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
