package com.neostain.csms.util.exception;

public class DuplicateFieldException extends Exception {
    private final String fieldName;

    public DuplicateFieldException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
