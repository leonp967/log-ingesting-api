package com.leonp967.log.ingesting.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompositeMetricDTO that = (CompositeMetricDTO) o;

        return Objects.equals(this.key, that.key) &&
                Objects.equals(this.values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, values);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("key = " + key)
                .add("values = " + values)
                .toString();
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
