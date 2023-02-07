package com.orangeandbronze.enlistment;

import java.util.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

class LaboratorySubject  extends Subject{
    LaboratorySubject(String subjectId, int units, Collection<Subject> prerequisites){
        super(subjectId, units, prerequisites);
    }
}
