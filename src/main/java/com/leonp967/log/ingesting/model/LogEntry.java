package com.leonp967.log.ingesting.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class LogEntry {

    private String url;
    private Long timestamp;
    private String userUuid;
    private RegionEnum region;

    public static Builder builder() {
        return new Builder();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public RegionEnum getRegion() {
        return region;
    }

    public void setRegion(RegionEnum region) {
        this.region = region;
    }

    public static final class Builder {
        private String url;
        private Long timestamp;
        private String userUuid;
        private RegionEnum region;

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

        public Builder region(RegionEnum region) {
            this.region = region;
            return this;
        }

        public LogEntry build() {
            LogEntry logEntry = new LogEntry();
            logEntry.setUrl(url);
            logEntry.setTimestamp(timestamp);
            logEntry.setUserUuid(userUuid);
            logEntry.setRegion(region);
            return logEntry;
        }
    }
}
