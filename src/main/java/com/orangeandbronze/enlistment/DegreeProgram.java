package com.orangeandbronze.enlistment;

import java.util.*;
import static org.apache.commons.lang3.Validate.*;

class DegreeProgram {

    private final Collection<Subject> subjects = new HashSet<>();
    DegreeProgram(Collection<Subject> subjects){
        if (subjects == null) {
            throw new NullPointerException("subjects should not be null");
        }

        this.subjects.addAll(subjects);
    }

    DegreeProgram(){

    }

    void checkIfHasSubject(Subject subject){
        if (! this.subjects.contains(subject)){
            throw new NotInDegreeProgramException("Subject " + subject + " is not in degree program");
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjects);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
