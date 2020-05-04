package com.leonp967.log.ingesting.converter;

import com.leonp967.log.ingesting.bo.LogEntryBO;
import com.leonp967.log.ingesting.model.LogEntry;
import com.leonp967.log.ingesting.model.RegionEnum;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogEntryConverter {

    public LogEntryBO toBO(LogEntry logEntry) {
        if (logEntry == null) {
            throw new IllegalArgumentException("LogEntry cannot be null when converting to LogEntryBO!");
        }

        return LogEntryBO.builder()
                .accessTimestamp(logEntry.getAccessTimestamp())
                .region(RegionEnum.fromCode(logEntry.getRegion()).getDescription())
                .url(logEntry.getUrl())
                .userUuid(logEntry.getUserUuid())
                .build();
    }
}
