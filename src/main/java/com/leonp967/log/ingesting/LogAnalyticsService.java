package com.leonp967.log.ingesting;

import com.leonp967.log.ingesting.model.LogEntry;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;

@ApplicationScoped
public class LogAnalyticsService {

    public Uni<String> ingestLog(LogEntry logEntry) {
        return Uni.createFrom().item(logEntry)
                .onItem().apply(log -> "asd");
    }
}
