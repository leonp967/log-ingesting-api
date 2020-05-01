package com.leonp967.log.ingesting.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class CompositeMetricDTO {

    private String key;
    private List<MetricEntryDTO> values;

    public static Builder builder() {
        return new Builder();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<MetricEntryDTO> getValues() {
        return values;
    }

    public void setValues(List<MetricEntryDTO> values) {
        this.values = values;
    }

    public static final class Builder {
        private String key;
        private List<MetricEntryDTO> values;

        private Builder() {
        }

        public static Builder aComposedMetricBO() {
            return new Builder();
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder values(List<MetricEntryDTO> values) {
            this.values = values;
            return this;
        }

        public CompositeMetricDTO build() {
            CompositeMetricDTO compositeMetricDTO = new CompositeMetricDTO();
            compositeMetricDTO.setKey(key);
            compositeMetricDTO.setValues(values);
            return compositeMetricDTO;
        }
    }
}
