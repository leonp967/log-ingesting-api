package com.leonp967.log.ingesting.dto.converter;

import com.leonp967.log.ingesting.bo.CompositeMetricBO;
import com.leonp967.log.ingesting.bo.MetricEntryBO;
import com.leonp967.log.ingesting.bo.MetricsBO;
import com.leonp967.log.ingesting.dto.CompositeMetricDTO;
import com.leonp967.log.ingesting.dto.MetricEntryDTO;
import com.leonp967.log.ingesting.dto.MetricsDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class MetricsConverter {

    private final MetricEntryConverter metricEntryConverter;
    private final CompositeMetricConverter compositeMetricConverter;

    public MetricsConverter(MetricEntryConverter metricEntryConverter, CompositeMetricConverter compositeMetricConverter) {
        this.metricEntryConverter = metricEntryConverter;
        this.compositeMetricConverter = compositeMetricConverter;
    }

    public MetricsDTO toDTO(MetricsBO metricsBO) {
        return MetricsDTO.builder()
                .bottomUrl(metricEntryConverter.toDTO(metricsBO.getBottomUrl()))
                .mostAccessedMinute(metricEntryConverter.toDTO(metricsBO.getMostAccessedMinute()))
                .topUrls(convertMetricEntryList(metricsBO.getTopUrls()))
                .topUrlsByRegion(convertCompositeMetricList(metricsBO.getTopUrlsByRegion()))
                .topUrlsByTime(convertCompositeMetricList(metricsBO.getTopUrlsByTime()))
                .build();
    }

    private List<CompositeMetricDTO> convertCompositeMetricList(List<CompositeMetricBO> compositeMetricBOs) {
        return compositeMetricBOs.stream()
                .map(compositeMetricConverter::toDTO)
                .collect(Collectors.toList());
    }

    private List<MetricEntryDTO> convertMetricEntryList(List<MetricEntryBO> metricEntryBOs) {
        return metricEntryBOs.stream()
                .map(metricEntryConverter::toDTO)
                .collect(Collectors.toList());
    }
}
