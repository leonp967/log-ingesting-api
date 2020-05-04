package com.leonp967.log.ingesting.bo;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class CompositeMetricBO {

    private String key;
    private List<MetricEntryBO> values;

    public static Builder builder() {
        return new Builder();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<MetricEntryBO> getValues() {
        return values;
    }

    public void setValues(List<MetricEntryBO> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CompositeMetricBO that = (CompositeMetricBO) o;

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
        private List<MetricEntryBO> values;

        private Builder() {
        }

        public static Builder aComposedMetricBO() {
            return new Builder();
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder values(List<MetricEntryBO> values) {
            this.values = values;
            return this;
        }

        public CompositeMetricBO build() {
            CompositeMetricBO compositeMetricBO = new CompositeMetricBO();
            compositeMetricBO.setKey(key);
            compositeMetricBO.setValues(values);
            return compositeMetricBO;
        }
    }
}
