package com.leonp967.log.ingesting.resource;

import com.leonp967.log.ingesting.bo.converter.LogEntryConverter;
import com.leonp967.log.ingesting.model.LogEntry;
import com.leonp967.log.ingesting.model.MetricsFilter;
import com.leonp967.log.ingesting.service.LogAnalyticsService;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/laa")
public class LogAnalyticsResource {

    private final LogAnalyticsService service;
    private final LogEntryConverter converter;

    public LogAnalyticsResource(LogAnalyticsService service, LogEntryConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GET
    @Path("/health")
    public Response health() {
        return Response.ok().build();
    }

    @GET
    @Path("/metrics")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> metrics(MetricsFilter metricsFilter) {
        return service.evaluateMetrics(metricsFilter)
                .onItem().apply(metricsBO ->
                        Response.ok(metricsBO).build());
    }

    @POST
    @Path("/ingest")
    public Response ingest(LogEntry logEntry) {
        service.ingestLog(converter.toBO(logEntry));
        return Response.ok().build();
    }
}
