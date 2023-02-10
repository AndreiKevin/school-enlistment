package com.orangeandbronze.enlistment;

import java.util.*;
import static org.apache.commons.lang3.Validate.*;

class StudentBuilder {
    private int studentNumber = -1;
    private Collection<Section> enrolledSections = new HashSet<>();
    private Collection<Subject> takenSubjects = new HashSet<>();
    private DegreeProgram degreeProgram = new DegreeProgram();

    StudentBuilder(){

    }

    Student getResult() {
        if(studentNumber < 0) {
            throw new IllegalArgumentException(
                    "studentNumber should be non-negative, was: "  + studentNumber);
        }
        notNull(degreeProgram);

        return new Student(studentNumber, degreeProgram, enrolledSections, takenSubjects);
    }
    StudentBuilder setStudentNumber(int studentNumber){
        if(studentNumber < 0) {
            throw new IllegalArgumentException(
                    "studentNumber should be non-negative, was: "  + studentNumber);
        }

        this.studentNumber = studentNumber;

        return this;
    }

    StudentBuilder setEnrolledSections(Collection<Section> enrolledSections){
        notNull(enrolledSections);

        this.enrolledSections.addAll(enrolledSections);
        this.enrolledSections.removeIf(Objects::isNull);

        return this;
    }

    StudentBuilder setTakenSubjects(Collection<Subject> takenSubjects){
        notNull(takenSubjects);

        this.takenSubjects.addAll(takenSubjects);
        this.takenSubjects.removeIf(Objects::isNull);
        return this;
    }

    StudentBuilder setDegreeProgram(DegreeProgram degreeProgram) {
        notNull(degreeProgram);
        this.degreeProgram = degreeProgram;
        return this;
    }
}
