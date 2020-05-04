package com.leonp967.log.ingesting.builder;

import com.leonp967.log.ingesting.bo.MetricsBO;
import com.leonp967.log.ingesting.dto.MetricsDTO;

import static java.util.Collections.singletonList;

public class MetricsBuilder {

    public static MetricsBO buildBO() {
        return MetricsBO.builder()
                .topUrlsByTime(singletonList(CompositeMetricBuilder.buildBO()))
                .topUrls(singletonList(MetricEntryBuilder.buildBO()))
                .topUrlsByRegion(singletonList(CompositeMetricBuilder.buildBO()))
                .mostAccessedMinute(MetricEntryBuilder.buildBO())
                .bottomUrl(MetricEntryBuilder.buildBO())
                .build();
    }

    public static MetricsDTO buildDTO() {
        return MetricsDTO.builder()
                .topUrlsByTime(singletonList(CompositeMetricBuilder.buildDTO()))
                .topUrls(singletonList(MetricEntryBuilder.buildDTO()))
                .topUrlsByRegion(singletonList(CompositeMetricBuilder.buildDTO()))
                .mostAccessedMinute(MetricEntryBuilder.buildDTO())
                .bottomUrl(MetricEntryBuilder.buildDTO())
                .build();
    }
}
