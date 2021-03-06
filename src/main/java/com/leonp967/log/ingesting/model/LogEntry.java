package com.leonp967.log.ingesting.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Objects;

@RegisterForReflection
public class LogEntry {

    private String url;
    private Long accessTimestamp;
    private String userUuid;
    private Integer region;

    public static Builder builder() {
        return new Builder();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getAccessTimestamp() {
        return accessTimestamp;
    }

    public void setAccessTimestamp(Long accessTimestamp) {
        this.accessTimestamp = accessTimestamp;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntry logEntry = (LogEntry) o;
        return Objects.equals(url, logEntry.url) &&
                Objects.equals(accessTimestamp, logEntry.accessTimestamp) &&
                Objects.equals(userUuid, logEntry.userUuid) &&
                Objects.equals(region, logEntry.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, accessTimestamp, userUuid, region);
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "url='" + url + '\'' +
                ", accessTimestamp=" + accessTimestamp +
                ", userUuid='" + userUuid + '\'' +
                ", region='" + region + '\'' +
                '}';
    }

    public static final class Builder {
        private String url;
        private Long timestamp;
        private String userUuid;
        private Integer region;

        private Builder() {
        }

        public static Builder aLogEntry() {
            return new Builder();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder timestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder userUuid(String userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public Builder region(Integer region) {
            this.region = region;
            return this;
        }

        public LogEntry build() {
            LogEntry logEntry = new LogEntry();
            logEntry.setUrl(url);
            logEntry.setAccessTimestamp(timestamp);
            logEntry.setUserUuid(userUuid);
            logEntry.setRegion(region);
            return logEntry;
        }
    }
}
