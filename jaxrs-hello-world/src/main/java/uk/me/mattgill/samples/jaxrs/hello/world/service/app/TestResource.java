package uk.me.mattgill.samples.jaxrs.hello.world.service.app;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/test")
public class TestResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String[] helloWorld(@QueryParam("callbackUrl") String callbackUrl) {
        return new String[] { "Hello World!", "Testing!" };
    }

}