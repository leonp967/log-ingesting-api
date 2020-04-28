package com.leonp967.log.ingesting.resource;

import com.leonp967.log.ingesting.bo.LogEntryConverter;
import com.leonp967.log.ingesting.bo.MetricsBO;
import com.leonp967.log.ingesting.model.TimeTypeEnum;
import com.leonp967.log.ingesting.service.LogAnalyticsService;
import com.leonp967.log.ingesting.model.LogEntry;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/laa")
public class LogAnalyticsResource {

    LogAnalyticsService service;
    LogEntryConverter converter;

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
    public Uni<Response> metrics() throws IOException {
        return service.evaluateMetrics(25, TimeTypeEnum.DAY)
                .onItem().apply(metricsBO -> Response.ok(metricsBO).build());
    }

    @POST
    @Path("/ingest")
    public Response ingest(LogEntry logEntry) {
        service.ingestLog(converter.toBO(logEntry));
        return Response.ok().build();
    }
}
