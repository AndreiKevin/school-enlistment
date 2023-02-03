package com.orangeandbronze.enlistment;

import java.util.*;

import static org.apache.commons.lang3.Validate.*;

class Student {
    private final int studentNumber;
    private final Collection<Section> sections = new HashSet<>();

    Student(int studentNumber, Collection<Section> sections) {
        if(studentNumber < 0) {
            throw new IllegalArgumentException(
                    "studentNumber should be non-negative, was: "  + studentNumber);
        }
        if (sections == null) {
            throw new NullPointerException("sections should not be null");
        }

        this.studentNumber = studentNumber;
        this.sections.addAll(sections);
        this.sections.removeIf(Objects::isNull);
    }

    Student(int studentNumber) {
        this(studentNumber, Collections.emptyList());
    }


    public void enlist(Section newSection) {
        notNull(newSection);
        sections.forEach( currSection -> currSection.checkForConflict(newSection));
        sections.add(newSection);
        newSection.addStudent();
    }

    public void cancel(Section section){
        notNull(section);
        if(!sections.contains(section)) {
            throw new CancelNotEnlistedSectionException("Cannot cancel enlistment because student is not enlisted in section. student was "
                    + this + ", section was " + section);
        }

        sections.remove(section);
        section.removeStudent();
    }

    public Collection<Section> getSections() {
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
