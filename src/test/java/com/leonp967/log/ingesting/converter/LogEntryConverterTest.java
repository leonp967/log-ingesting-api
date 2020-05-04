package com.leonp967.log.ingesting.converter;

import com.leonp967.log.ingesting.bo.LogEntryBO;
import com.leonp967.log.ingesting.builder.LogEntryBuilder;
import com.leonp967.log.ingesting.model.LogEntry;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LogEntryConverterTest {

    private static LogEntryConverter converter;

    @BeforeAll
    public static void setUp() {
        converter = new LogEntryConverter();
    }

    @Test
    public void shouldConvertModelToBO() {
        LogEntry model = LogEntryBuilder.buildModel();
        LogEntryBO expected = LogEntryBuilder.buildBO();

        LogEntryBO result = converter.toBO(model);

        assertThat(result, is(expected));
    }

    @Test
    public void shouldCheckNullModel() {
        assertThrows(IllegalArgumentException.class,
                () -> converter.toBO(null),
                "LogEntry cannot be null when converting to LogEntryBO!");
    }

}