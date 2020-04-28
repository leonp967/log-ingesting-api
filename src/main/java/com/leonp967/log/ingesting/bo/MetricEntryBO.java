package com.leonp967.log.ingesting.bo;

public class MetricEntryBO {

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

        public MetricEntryBO build() {
            MetricEntryBO metricEntryBO = new MetricEntryBO();
            metricEntryBO.setKey(key);
            metricEntryBO.setCount(count);
            return metricEntryBO;
        }
    }
}
