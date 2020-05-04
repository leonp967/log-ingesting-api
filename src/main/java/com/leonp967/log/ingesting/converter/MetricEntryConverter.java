package com.leonp967.log.ingesting.converter;

import com.leonp967.log.ingesting.bo.MetricEntryBO;
import com.leonp967.log.ingesting.dto.MetricEntryDTO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MetricEntryConverter {

    public MetricEntryDTO toDTO(MetricEntryBO metricEntryBO) {
        if(metricEntryBO == null) {
            throw new IllegalArgumentException("MetricEntryBO cannot be null when converting to MetricEntryDTO!");
        }

        return MetricEntryDTO.builder()
                .count(metricEntryBO.getCount())
                .key(metricEntryBO.getKey())
                .build();
    }
}
