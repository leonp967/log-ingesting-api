package com.leonp967.log.ingesting.builder;

import com.leonp967.log.ingesting.bo.CompositeMetricBO;
import com.leonp967.log.ingesting.dto.CompositeMetricDTO;

import static java.util.Collections.singletonList;

public class CompositeMetricBuilder {

    private static final String KEY = "time";

    public static CompositeMetricBO buildBO() {
        return CompositeMetricBO.builder()
                .key(KEY)
                .values(singletonList(MetricEntryBuilder.buildBO()))
                .build();
    }

    public static CompositeMetricDTO buildDTO() {
        return CompositeMetricDTO.builder()
                .key(KEY)
                .values(singletonList(MetricEntryBuilder.buildDTO()))
                .build();
    }
}
