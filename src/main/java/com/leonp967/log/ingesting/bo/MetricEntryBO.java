package com.leonp967.log.ingesting.bo;

import java.util.Objects;
import java.util.StringJoiner;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetricEntryBO that = (MetricEntryBO) o;

        return Objects.equals(this.count, that.count) &&
                Objects.equals(this.key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, key);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("count = " + count)
                .add("key = " + key)
                .toString();
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
