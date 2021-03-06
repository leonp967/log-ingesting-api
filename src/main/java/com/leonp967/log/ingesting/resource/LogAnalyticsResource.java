package com.leonp967.log.ingesting.resource;

import com.leonp967.log.ingesting.converter.LogEntryConverter;
import com.leonp967.log.ingesting.converter.MetricsConverter;
import com.leonp967.log.ingesting.model.LogEntry;
import com.leonp967.log.ingesting.model.MetricsFilter;
import com.leonp967.log.ingesting.service.LogAnalyticsService;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/laa")
public class LogAnalyticsResource {

    private final LogAnalyticsService service;
    private final LogEntryConverter logEntryConverter;
    private final MetricsConverter metricsConverter;

    public LogAnalyticsResource(LogAnalyticsService service, LogEntryConverter logEntryConverter,
                                MetricsConverter metricsConverter) {
        this.service = service;
        this.logEntryConverter = logEntryConverter;
        this.metricsConverter = metricsConverter;
    }

    @GET
    @Path("/health")
    public Uni<Response> health() {
        return service.healthCheck()
                .onItem()
                .apply(status -> Response.status(status).build());
    }

    @GET
    @Path("/metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> metrics(MetricsFilter metricsFilter) {
        return service.evaluateMetrics(metricsFilter)
                .onItem().apply(metricsBO ->
                        Response.ok(metricsConverter.toDTO(metricsBO)).build());
    }

    @POST
    @Path("/ingest")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> ingest(LogEntry logEntry) {
        return service.ingestLog(logEntryConverter.toBO(logEntry))
                .onItem().apply(status -> {
                    if (status == Response.Status.OK) {
                        return Response.status(Response.Status.CREATED).build();
                    } else {
                        return Response.status(Response.Status.BAD_REQUEST).build();
                    }
                });
    }
}
