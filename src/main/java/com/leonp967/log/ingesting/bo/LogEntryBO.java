package com.leonp967.log.ingesting.bo;

import java.util.Objects;
import java.util.StringJoiner;

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
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .add("accessTimestamp = " + accessTimestamp)
                .add("region = " + region)
                .add("url = " + url)
                .add("userUuid = " + userUuid)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogEntryBO that = (LogEntryBO) o;

        return Objects.equals(this.accessTimestamp, that.accessTimestamp) &&
                Objects.equals(this.region, that.region) &&
                Objects.equals(this.url, that.url) &&
                Objects.equals(this.userUuid, that.userUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessTimestamp, region, url, userUuid);
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
