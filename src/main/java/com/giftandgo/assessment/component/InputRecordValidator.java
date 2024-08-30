package com.giftandgo.assessment.component;

import org.apache.commons.csv.CSVRecord;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InputRecordValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isInstance(CSVRecord.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CSVRecord record = (CSVRecord) target;

        if (record.size() != 7) {
            errors.reject("Invalid record size");
            return;
        }

        if (record.get(0).length() != 36) {
            errors.reject("Invalid UUID");
        }

        if (record.get(1).length() != 6) {
            errors.reject("Invalid ID");
        }

        if (record.get(2).isEmpty()) {
            errors.reject("Invalid name");
        }

        if (record.get(3).isEmpty()) {
            errors.reject("Invalid likes");
        }

        if (record.get(4).isEmpty()) {
            errors.reject("Invalid transport");
        }

        try {
            Double.parseDouble(record.get(5));
        } catch (NumberFormatException e) {
            errors.reject("Invalid avgSpeed");
        }

        try {
            Double.parseDouble(record.get(6));
        } catch (NumberFormatException e) {
            errors.reject("Invalid topSpeed");
        }
    }
}
