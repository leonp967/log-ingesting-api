package com.leonp967.log.ingesting.service;

import com.leonp967.log.ingesting.bo.LogEntryBO;
import com.leonp967.log.ingesting.bo.MetricsBO;
import com.leonp967.log.ingesting.model.MetricsFilter;
import com.leonp967.log.ingesting.model.TimeTypeEnum;
import com.leonp967.log.ingesting.repository.LogAnalyticsRepository;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class LogAnalyticsService {

    private final Vertx vertx;
    private WebClient webClient;
    private final LogAnalyticsRepository repository;
    private final Logger LOGGER = LoggerFactory.getLogger(LogAnalyticsService.class);

    public LogAnalyticsService(Vertx vertx, LogAnalyticsRepository repository) {
        this.vertx = vertx;
        this.repository = repository;
    }

    @PostConstruct
    public void initialize() {
        webClient = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost("localhost")
                        .setDefaultPort(8080));
    }

    @CacheInvalidateAll(cacheName = "metrics")
    public void ingestLog(LogEntryBO logEntry) {
        webClient.post("")
                .sendJson(logEntry, result -> {
                    if (result.succeeded()) {
                        LOGGER.info("Log successfully sent to Logstash: {}", logEntry);
                    } else {
                        LOGGER.error("Error sending log to Logstash", result.cause());
                    }
                });
    }

    @CacheResult(cacheName = "metrics")
    public Uni<MetricsBO> evaluateMetrics(MetricsFilter metricsFilter) {
        return repository.evaluateAllMetrics(metricsFilter.getTimeValue(),
                TimeTypeEnum.fromCode(metricsFilter.getTimeType()));
    }
}
