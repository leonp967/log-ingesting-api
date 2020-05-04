package com.leonp967.log.ingesting.converter;

import com.leonp967.log.ingesting.bo.CompositeMetricBO;
import com.leonp967.log.ingesting.bo.MetricEntryBO;
import com.leonp967.log.ingesting.dto.CompositeMetricDTO;
import com.leonp967.log.ingesting.dto.MetricEntryDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CompositeMetricConverter {

    private final MetricEntryConverter metricEntryConverter;

    public CompositeMetricConverter(MetricEntryConverter metricEntryConverter) {
        this.metricEntryConverter = metricEntryConverter;
    }

    public CompositeMetricDTO toDTO(CompositeMetricBO compositeMetricBO) {
        if (compositeMetricBO == null) {
            throw new IllegalArgumentException("CompositeMetricBO cannot be null when converting to CompositeMetricDTO!");
        }

        return CompositeMetricDTO.builder()
                .key(compositeMetricBO.getKey())
                .values(convertList(compositeMetricBO.getValues()))
                .build();
    }

    private List<MetricEntryDTO> convertList(List<MetricEntryBO> metricEntryBOs) {
        if (metricEntryBOs == null) {
            throw new IllegalArgumentException("List<MetricEntryBO> cannot be null when converting to CompositeMetricDTO!");
        }

        return metricEntryBOs.stream()
                .map(metricEntryConverter::toDTO)
                .collect(Collectors.toList());
    }
}
