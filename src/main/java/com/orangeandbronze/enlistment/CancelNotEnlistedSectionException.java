package com.orangeandbronze.enlistment;

public class CancelNotEnlistedSectionException extends RuntimeException {
    public CancelNotEnlistedSectionException(String studentStr, String sectionStr) {
        super("Cannot cancel enlistment because student is not enlisted in section. student was "
                + studentStr + ", section was " + sectionStr);
    }
}
