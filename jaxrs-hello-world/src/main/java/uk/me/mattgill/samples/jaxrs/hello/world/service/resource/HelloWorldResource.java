package uk.me.mattgill.samples.jaxrs.hello.world.service.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import uk.me.mattgill.samples.jaxrs.hello.world.api.TracedEndpoint;

@Path("/")
public class HelloWorldResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @TracedEndpoint
    public String helloWorld() {
        return "Hello World!";
    }
}
