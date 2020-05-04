package com.leonp967.log.ingesting.builder;

import com.leonp967.log.ingesting.bo.MetricEntryBO;
import com.leonp967.log.ingesting.dto.MetricEntryDTO;

public class MetricEntryBuilder {

    private static final String KEY = "region";
    private static final Long COUNT = 123L;

    public static MetricEntryBO buildBO() {
        return MetricEntryBO.builder()
                .key(KEY)
                .count(COUNT)
                .build();
    }

    public static MetricEntryDTO buildDTO() {
        return MetricEntryDTO.builder()
                .key(KEY)
                .count(COUNT)
                .build();
    }
}
