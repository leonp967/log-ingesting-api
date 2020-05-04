package com.leonp967.log.ingesting.converter;

import com.leonp967.log.ingesting.bo.CompositeMetricBO;
import com.leonp967.log.ingesting.builder.CompositeMetricBuilder;
import com.leonp967.log.ingesting.dto.CompositeMetricDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;

public class CompositeMetricConverterTest {

    private static CompositeMetricConverter converter;

    @BeforeAll
    public static void setUp() {
        MetricEntryConverter metricEntryConverter = spy(MetricEntryConverter.class);
        converter = new CompositeMetricConverter(metricEntryConverter);
    }

    @Test
    public void shouldConvertBOtoDTO() {
        CompositeMetricBO bo = CompositeMetricBuilder.buildBO();
        CompositeMetricDTO expected = CompositeMetricBuilder.buildDTO();

        CompositeMetricDTO result = converter.toDTO(bo);

        assertThat(result, is(expected));
    }

    @Test
    public void shouldCheckNullBO() {
        assertThrows(IllegalArgumentException.class,
                () -> converter.toDTO(null),
                "CompositeMetricBO cannot be null when converting to CompositeMetricDTO!");
    }

    @Test
    public void shoudlCheckNullList() {
        CompositeMetricBO bo = CompositeMetricBuilder.buildBO();
        bo.setValues(null);

        assertThrows(IllegalArgumentException.class,
                () -> converter.toDTO(bo),
                "List<MetricEntryBO> cannot be null when converting to CompositeMetricDTO!");
    }

}