package com.leonp967.log.ingesting.bo;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class MetricsBO {

    private List<MetricEntryBO> topUrls;
    private List<CompositeMetricBO> topUrlsByRegion;
    private MetricEntryBO bottomUrl;
    private List<CompositeMetricBO> topUrlsByTime;
    private MetricEntryBO mostAccessedMinute;

    public static Builder builder() {
        return new Builder();
    }

    public List<MetricEntryBO> getTopUrls() {
        return topUrls;
    }

    public void setTopUrls(List<MetricEntryBO> topUrls) {
        this.topUrls = topUrls;
    }

    public List<CompositeMetricBO> getTopUrlsByRegion() {
        return topUrlsByRegion;
    }

    public void setTopUrlsByRegion(List<CompositeMetricBO> topUrlsByRegion) {
        this.topUrlsByRegion = topUrlsByRegion;
    }

    public MetricEntryBO getBottomUrl() {
        return bottomUrl;
    }

    public void setBottomUrl(MetricEntryBO bottomUrl) {
        this.bottomUrl = bottomUrl;
    }

    public List<CompositeMetricBO> getTopUrlsByTime() {
        return topUrlsByTime;
    }

    public void setTopUrlsByTime(List<CompositeMetricBO> topUrlsByTime) {
        this.topUrlsByTime = topUrlsByTime;
    }

    public MetricEntryBO getMostAccessedMinute() {
        return mostAccessedMinute;
    }

    public void setMostAccessedMinute(MetricEntryBO mostAccessedMinute) {
        this.mostAccessedMinute = mostAccessedMinute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MetricsBO that = (MetricsBO) o;

        return Objects.equals(this.bottomUrl, that.bottomUrl) &&
                Objects.equals(this.mostAccessedMinute, that.mostAccessedMinute) &&
                Objects.equals(this.topUrls, that.topUrls) &&
                Objects.equals(this.topUrlsByRegion, that.topUrlsByRegion) &&
                Objects.equals(this.topUrlsByTime, that.topUrlsByTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bottomUrl, mostAccessedMinute, topUrls, topUrlsByRegion, topUrlsByTime);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("bottomUrl = " + bottomUrl)
                .add("mostAccessedMinute = " + mostAccessedMinute)
                .add("topUrls = " + topUrls)
                .add("topUrlsByRegion = " + topUrlsByRegion)
                .add("topUrlsByTime = " + topUrlsByTime)
                .toString();
    }

    public static final class Builder {
        private List<MetricEntryBO> topUrls;
        private List<CompositeMetricBO> topUrlsByRegion;
        private MetricEntryBO bottomUrl;
        private List<CompositeMetricBO> topUrlsByTime;
        private MetricEntryBO mostAccessedMinute;

        private Builder() {
        }

        public static Builder aMetricsBO() {
            return new Builder();
        }

        public Builder topUrls(List<MetricEntryBO> topUrls) {
            this.topUrls = topUrls;
            return this;
        }

        public Builder topUrlsByRegion(List<CompositeMetricBO> topUrlsByRegion) {
            this.topUrlsByRegion = topUrlsByRegion;
            return this;
        }

        public Builder bottomUrl(MetricEntryBO bottomUrl) {
            this.bottomUrl = bottomUrl;
            return this;
        }

        public Builder topUrlsByTime(List<CompositeMetricBO> topUrlsByTime) {
            this.topUrlsByTime = topUrlsByTime;
            return this;
        }

        public Builder mostAccessedMinute(MetricEntryBO mostAccessedMinute) {
            this.mostAccessedMinute = mostAccessedMinute;
            return this;
        }

        public MetricsBO build() {
            MetricsBO metricsBO = new MetricsBO();
            metricsBO.setTopUrls(topUrls);
            metricsBO.setTopUrlsByRegion(topUrlsByRegion);
            metricsBO.setBottomUrl(bottomUrl);
            metricsBO.setTopUrlsByTime(topUrlsByTime);
            metricsBO.setMostAccessedMinute(mostAccessedMinute);
            return metricsBO;
        }
    }
}
