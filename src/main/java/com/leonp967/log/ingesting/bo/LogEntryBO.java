package com.leonp967.log.ingesting.bo;

import com.leonp967.log.ingesting.model.RegionEnum;

public class LogEntryBO {

    private String url;
    private Long accessTimestamp;
    private String userUuid;
    private String region;

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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "LogEntryBO{" +
                "url='" + url + '\'' +
                ", accessTimestamp=" + accessTimestamp +
                ", userUuid='" + userUuid + '\'' +
                ", region=" + region +
                '}';
    }

    public static final class Builder {
        private String url;
        private Long accessTimestamp;
        private String userUuid;
        private String region;

        private Builder() {
        }

        public static Builder aLogEntryBO() {
            return new Builder();
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder accessTimestamp(Long accessTimestamp) {
            this.accessTimestamp = accessTimestamp;
            return this;
        }

        public Builder userUuid(String userUuid) {
            this.userUuid = userUuid;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public LogEntryBO build() {
            LogEntryBO logEntryBO = new LogEntryBO();
            logEntryBO.setUrl(url);
            logEntryBO.setAccessTimestamp(accessTimestamp);
            logEntryBO.setUserUuid(userUuid);
            logEntryBO.setRegion(region);
            return logEntryBO;
        }
    }
}
