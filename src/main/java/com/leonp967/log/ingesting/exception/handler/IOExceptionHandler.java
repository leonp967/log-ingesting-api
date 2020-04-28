package com.leonp967.log.ingesting.exception.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class IOExceptionHandler implements ExceptionMapper<IOException> {

    @Override
    public Response toResponse(IOException e) {
        return Response.serverError().entity(e.getMessage()).build();
    }
}
