package com.leonp967.log.ingesting.converter;

import com.leonp967.log.ingesting.bo.MetricsBO;
import com.leonp967.log.ingesting.builder.MetricsBuilder;
import com.leonp967.log.ingesting.dto.MetricsDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.spy;

public class MetricsConverterTest {

    private static MetricsConverter converter;

    @BeforeAll
    public static void setUp() {
        MetricEntryConverter metricEntryConverter = spy(MetricEntryConverter.class);
        CompositeMetricConverter compositeMetricConverter = new CompositeMetricConverter(metricEntryConverter);

        converter = new MetricsConverter(metricEntryConverter, compositeMetricConverter);
    }

    @Test
    public void shouldConvertBOtoDTO() {
        MetricsBO bo = MetricsBuilder.buildBO();
        MetricsDTO expected = MetricsBuilder.buildDTO();

        MetricsDTO result = converter.toDTO(bo);

        assertThat(result, is(expected));
    }

    @Test
    public void shouldCheckNullBO() {
        assertThrows(IllegalArgumentException.class,
                () -> converter.toDTO(null),
                "MetricsBO cannot be null when converting to MetricsDTO!");
    }

    @Test
    public void shouldCheckNullCompositeList() {
        MetricsBO bo = MetricsBuilder.buildBO();
        bo.setTopUrlsByRegion(null);

        assertThrows(IllegalArgumentException.class,
                () -> converter.toDTO(bo),
                "List<CompositeMetricBO> cannot be null when converting to MetricsDTO!");
    }

    @Test
    public void shouldCheckNullEntryList() {
        MetricsBO bo = MetricsBuilder.buildBO();
        bo.setTopUrls(null);

        assertThrows(IllegalArgumentException.class,
                () -> converter.toDTO(bo),
                "List<MetricEntryBO> cannot be null when converting to MetricsDTO!");
    }

}