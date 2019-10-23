package uk.me.mattgill.samples.arquillian;

import javax.ws.rs.GET;
import javax.ws.rs.core.Response;

public interface Endpoint {

    @GET
    public Response getResponse();
}