package com.orangeandbronze.enlistment;

import java.math.BigDecimal;
import java.util.*;

import static org.apache.commons.lang3.Validate.*;

class Student {
    private final int studentNumber;
    private final Collection<Section> sections = new HashSet<>();
    private final Collection<Subject> takenSubjects = new HashSet<>();

    Student(int studentNumber, Collection<Section> sections, Collection<Subject> takenSubjects) {
        if(studentNumber < 0) {
            throw new IllegalArgumentException(
                    "studentNumber should be non-negative, was: "  + studentNumber);
        }
        notNull(sections);
        notNull(takenSubjects);

        this.studentNumber = studentNumber;

        this.sections.addAll(sections);
        this.sections.removeIf(Objects::isNull);
        this.sections.forEach( currSection -> currSection.addStudent());

        this.takenSubjects.addAll(takenSubjects);
        this.takenSubjects.removeIf(Objects::isNull);
    }

    Student(int studentNumber, Collection<Section> sections) {
        this(studentNumber, sections, Collections.emptyList());
    }

    Student(int studentNumber) {
        this(studentNumber, Collections.emptyList(), Collections.emptyList());
    }

    void enlist(Section newSection) {
        notNull(newSection);
        sections.forEach( currSection -> currSection.checkForConflict(newSection));
        newSection.checkIfPrerequisitesTaken(takenSubjects);

        sections.add(newSection);
        newSection.addStudent();
    }

    void cancel(Section section){
        notNull(section);
        if(!sections.contains(section)) {
            throw new CancelNotEnlistedSectionException("Cannot cancel enlistment because student is not enlisted in section. student was "
                    + this + ", section was " + section);
        }

        sections.remove(section);
        section.removeStudent();
    }

    BigDecimal requestAssessment(){
        return AssessmentHandler.assess(sections);
    }

    Collection<Section> getSections() {
        return new ArrayList<>(sections);
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
