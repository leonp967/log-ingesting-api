package com.leonp967.log.ingesting.service;

import com.leonp967.log.ingesting.bo.LogEntryBO;
import com.leonp967.log.ingesting.bo.MetricsBO;
import com.leonp967.log.ingesting.model.MetricsFilter;
import com.leonp967.log.ingesting.model.TimeTypeEnum;
import com.leonp967.log.ingesting.repository.LogAnalyticsRepository;
import io.quarkus.cache.CacheInvalidateAll;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

@Singleton
public class LogAnalyticsService {

    private final Vertx vertx;
    private WebClient webClient;
    private final LogAnalyticsRepository repository;
    private final Logger LOGGER = LoggerFactory.getLogger(LogAnalyticsService.class);

    @ConfigProperty(name = "logstash.url")
    String logstashUrl;

    @ConfigProperty(name = "logstash.port")
    Integer logstashPort;

    public LogAnalyticsService(Vertx vertx, LogAnalyticsRepository repository) {
        this.vertx = vertx;
        this.repository = repository;
    }

    @PostConstruct
    public void initialize() {
        webClient = WebClient.create(vertx,
                new WebClientOptions().setDefaultHost(logstashUrl)
                        .setDefaultPort(logstashPort));
    }

    @CacheInvalidateAll(cacheName = "metrics")
    public Uni<Response.Status> ingestLog(LogEntryBO logEntry) {
        return webClient.post("")
                .sendJson(logEntry)
                .onItem()
                .apply( response -> {
                    if (Response.Status.OK.getStatusCode() == response.statusCode()) {
                        LOGGER.info("Log successfully sent to Logstash: {}", logEntry);
                    } else {
                        LOGGER.error("Error sending log to Logstash: {}", response.bodyAsString());
                    }
                    return Response.Status.fromStatusCode(response.statusCode());
        });
    }

    @CacheResult(cacheName = "metrics")
    public Uni<MetricsBO> evaluateMetrics(MetricsFilter metricsFilter) {
        return repository.evaluateAllMetrics(metricsFilter.getTimeValue(),
                TimeTypeEnum.fromCode(metricsFilter.getTimeType()));
    }

    public Uni<Response.Status> healthCheck() {
        return Uni.combine().all()
                .unis(repository.healthCheck(), logstashHealthCheck())
                .asTuple()
                .onItem()
                .apply(statuses -> {
                    if (statuses.getItem1() == Response.Status.OK
                        && statuses.getItem2() == Response.Status.OK) {
                        return Response.Status.OK;
                    }
                    return Response.Status.INTERNAL_SERVER_ERROR;
                });
    }

    private Uni<Response.Status> logstashHealthCheck() {
        return webClient.get("")
                .send()
                .onItem()
                .apply(response -> {
                    LOGGER.info("Logstash health check status [{}] and message [{}]", response.statusCode(), response.bodyAsString());
                    return Response.Status.fromStatusCode(response.statusCode());
                });
    }
}
