package com.leonp967.log.ingesting.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class MetricEntryDTO {

    private String key;
    private Long count;

    public static Builder builder() {
        return new Builder();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public static final class Builder {
        private String key;
        private Long count;

        private Builder() {
        }

        public static Builder aMetricEntryBO() {
            return new Builder();
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder count(Long count) {
            this.count = count;
            return this;
        }

        public MetricEntryDTO build() {
            MetricEntryDTO metricEntryDTO = new MetricEntryDTO();
            metricEntryDTO.setKey(key);
            metricEntryDTO.setCount(count);
            return metricEntryDTO;
        }
    }
}
