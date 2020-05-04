package com.leonp967.log.ingesting.builder;

import com.leonp967.log.ingesting.bo.LogEntryBO;
import com.leonp967.log.ingesting.model.LogEntry;
import com.leonp967.log.ingesting.model.RegionEnum;

public class LogEntryBuilder {

    private static final String URL = "https://google.com/?q=megamen";
    private static final Long ACCESS_TIMESTAMP = 1588564089000L;
    private static final String USER_UUID = "uuid";
    private static final Integer REGION = 1;

    public static LogEntry buildModel() {
        return LogEntry.builder()
                .region(REGION)
                .timestamp(ACCESS_TIMESTAMP)
                .url(URL)
                .userUuid(USER_UUID)
                .build();
    }

    public static LogEntryBO buildBO() {
        return LogEntryBO.builder()
                .region(RegionEnum.US_EAST.getDescription())
                .accessTimestamp(ACCESS_TIMESTAMP)
                .url(URL)
                .userUuid(USER_UUID)
                .build();
    }
}
