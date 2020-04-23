package com.leonp967.log.ingesting.resource;

import com.leonp967.log.ingesting.LogAnalyticsService;
import com.leonp967.log.ingesting.model.LogEntry;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/laa")
public class LogAnalyticsResource {

    @Inject
    private LogAnalyticsService service;

    @GET
    @Path("/health")
    public Response health() {
        return Response.ok().build();
    }

    @GET
    @Path("/metrics")
    public Response metrics() {
        return Response.ok().build();
    }

    @POST
    @Path("/ingest")
    public Uni<Response> ingest(LogEntry logEntry) {
        return service.ingestLog(logEntry)
                .onItem().apply(res -> Response.ok(res).build());
    }
}
