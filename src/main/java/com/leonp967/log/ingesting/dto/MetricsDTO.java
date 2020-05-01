package com.leonp967.log.ingesting.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class MetricsDTO {

    private List<MetricEntryDTO> topUrls;
    private List<CompositeMetricDTO> topUrlsByRegion;
    private MetricEntryDTO bottomUrl;
    private List<CompositeMetricDTO> topUrlsByTime;
    private MetricEntryDTO mostAccessedMinute;

    public static Builder builder() {
        return new Builder();
    }

    public List<MetricEntryDTO> getTopUrls() {
        return topUrls;
    }

    public void setTopUrls(List<MetricEntryDTO> topUrls) {
        this.topUrls = topUrls;
    }

    public List<CompositeMetricDTO> getTopUrlsByRegion() {
        return topUrlsByRegion;
    }

    public void setTopUrlsByRegion(List<CompositeMetricDTO> topUrlsByRegion) {
        this.topUrlsByRegion = topUrlsByRegion;
    }

    public MetricEntryDTO getBottomUrl() {
        return bottomUrl;
    }

    public void setBottomUrl(MetricEntryDTO bottomUrl) {
        this.bottomUrl = bottomUrl;
    }

    public List<CompositeMetricDTO> getTopUrlsByTime() {
        return topUrlsByTime;
    }

    public void setTopUrlsByTime(List<CompositeMetricDTO> topUrlsByTime) {
        this.topUrlsByTime = topUrlsByTime;
    }

    public MetricEntryDTO getMostAccessedMinute() {
        return mostAccessedMinute;
    }

    public void setMostAccessedMinute(MetricEntryDTO mostAccessedMinute) {
        this.mostAccessedMinute = mostAccessedMinute;
    }

    public static final class Builder {
        private List<MetricEntryDTO> topUrls;
        private List<CompositeMetricDTO> topUrlsByRegion;
        private MetricEntryDTO bottomUrl;
        private List<CompositeMetricDTO> topUrlsByTime;
        private MetricEntryDTO mostAccessedMinute;

        private Builder() {
        }

        public static Builder aMetricsBO() {
            return new Builder();
        }

        public Builder topUrls(List<MetricEntryDTO> topUrls) {
            this.topUrls = topUrls;
            return this;
        }

        public Builder topUrlsByRegion(List<CompositeMetricDTO> topUrlsByRegion) {
            this.topUrlsByRegion = topUrlsByRegion;
            return this;
        }

        public Builder bottomUrl(MetricEntryDTO bottomUrl) {
            this.bottomUrl = bottomUrl;
            return this;
        }

        public Builder topUrlsByTime(List<CompositeMetricDTO> topUrlsByTime) {
            this.topUrlsByTime = topUrlsByTime;
            return this;
        }

        public Builder mostAccessedMinute(MetricEntryDTO mostAccessedMinute) {
            this.mostAccessedMinute = mostAccessedMinute;
            return this;
        }

        public MetricsDTO build() {
            MetricsDTO metricsDTO = new MetricsDTO();
            metricsDTO.setTopUrls(topUrls);
            metricsDTO.setTopUrlsByRegion(topUrlsByRegion);
            metricsDTO.setBottomUrl(bottomUrl);
            metricsDTO.setTopUrlsByTime(topUrlsByTime);
            metricsDTO.setMostAccessedMinute(mostAccessedMinute);
            return metricsDTO;
        }
    }
}
