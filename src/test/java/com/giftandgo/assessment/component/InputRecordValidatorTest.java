package com.giftandgo.assessment.component;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.SimpleErrors;

import static org.junit.jupiter.api.Assertions.*;

class InputRecordValidatorTest {

    private InputRecordValidator inputRecordValidator = new InputRecordValidator();
    private CSVRecord record;

    @BeforeEach
    void setUp() {
        record = Mockito.mock(CSVRecord.class);
    }

    @Test
    void validateSuccess() {
        Mockito.when(record.size()).thenReturn(7);
        Mockito.when(record.get(0)).thenReturn("123456789012345678901234567890123456");
        Mockito.when(record.get(1)).thenReturn("123456");
        Mockito.when(record.get(2)).thenReturn("Name");
        Mockito.when(record.get(3)).thenReturn("Likes");
        Mockito.when(record.get(4)).thenReturn("Transport");
        Mockito.when(record.get(5)).thenReturn("1.0");
        Mockito.when(record.get(6)).thenReturn("2.0");

        SimpleErrors simpleErrors = new SimpleErrors(record);
        inputRecordValidator.validate(record, simpleErrors);

        Assertions.assertFalse(simpleErrors.hasErrors());
    }

    @Test
    void validateInvalidRecordSize() {
        Mockito.when(record.size()).thenReturn(6);
        Mockito.when(record.get(0)).thenReturn("123456789012345678901234567890123456");
        Mockito.when(record.get(1)).thenReturn("123456");
        Mockito.when(record.get(2)).thenReturn("Name");
        Mockito.when(record.get(3)).thenReturn("Likes");
        Mockito.when(record.get(4)).thenReturn("Transport");
        Mockito.when(record.get(5)).thenReturn("1.0");

        SimpleErrors simpleErrors = new SimpleErrors(record);
        inputRecordValidator.validate(record, simpleErrors);

        Assertions.assertTrue(simpleErrors.hasErrors());
        Assertions.assertEquals(1, simpleErrors.getAllErrors().size());
        Assertions.assertEquals("Invalid record size", simpleErrors.getAllErrors().get(0).getCode());
    }


}
