package com.leonp967.log.ingesting.bo;

import java.util.List;

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
