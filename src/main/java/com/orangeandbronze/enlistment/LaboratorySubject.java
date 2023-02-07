package com.orangeandbronze.enlistment;

import java.math.BigDecimal;
import java.util.*;
import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.Validate.*;

class LaboratorySubject  extends Subject{
    private final static BigDecimal LABORATORY_ADDITIONAL_FEES = new BigDecimal("1000");
    LaboratorySubject(String subjectId, int units, Collection<Subject> prerequisites){
        super(subjectId, units, prerequisites);
    }

    @Override
    BigDecimal getFees() {
        return super.getFees().add(LABORATORY_ADDITIONAL_FEES);
    }
}
