package com.leonp967.log.ingesting.converter;

import com.leonp967.log.ingesting.bo.MetricEntryBO;
import com.leonp967.log.ingesting.builder.MetricEntryBuilder;
import com.leonp967.log.ingesting.dto.MetricEntryDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MetricEntryConverterTest {

    private static MetricEntryConverter converter;

    @BeforeAll
    public static void setUp() {
        converter = new MetricEntryConverter();
    }

    @Test
    public void shouldConvertBOtoDTO() {
        MetricEntryBO bo = MetricEntryBuilder.buildBO();
        MetricEntryDTO expected = MetricEntryBuilder.buildDTO();

        MetricEntryDTO result = converter.toDTO(bo);

        assertThat(result, is(expected));
    }

    @Test
    public void shouldCheckNullBO() {
        assertThrows(IllegalArgumentException.class,
                () -> converter.toDTO(null),
                "MetricEntryBO cannot be null when converting to MetricEntryDTO!");
    }

}