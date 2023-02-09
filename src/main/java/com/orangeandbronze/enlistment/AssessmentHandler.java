package com.orangeandbronze.enlistment;

import java.math.BigDecimal;
import java.util.*;

class AssessmentHandler {
    private static final BigDecimal MISCELLANEOUS_FEES = new BigDecimal("3000");
    private static final BigDecimal VAT_MULTIPLIER = new BigDecimal("1.12");
    private AssessmentHandler() {

    }

    static BigDecimal assess(Collection<Section> sections){
        BigDecimal totalFees = getTotalFees(sections);
        return (totalFees.add(MISCELLANEOUS_FEES)).multiply(VAT_MULTIPLIER);
    }

    private static BigDecimal getTotalFees(Collection<Section> sections) {
        BigDecimal totalFees = new BigDecimal("0");
        Iterator<Section> sectionIterator = sections.iterator();

        while (sectionIterator.hasNext()){
            totalFees = totalFees.add(sectionIterator.next().getFees());
        }
        return totalFees;
    }
}
