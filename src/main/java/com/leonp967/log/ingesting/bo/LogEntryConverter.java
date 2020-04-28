package com.leonp967.log.ingesting.bo;

import com.leonp967.log.ingesting.model.LogEntry;
import com.leonp967.log.ingesting.model.RegionEnum;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class LogEntryConverter {

    public LogEntryBO toBO(LogEntry logEntry) {
        return LogEntryBO.builder()
                .accessTimestamp(logEntry.getAccessTimestamp())
                .region(RegionEnum.fromCode(logEntry.getRegion()))
                .url(logEntry.getUrl())
                .userUuid(logEntry.getUserUuid())
                .build();
    }
}
