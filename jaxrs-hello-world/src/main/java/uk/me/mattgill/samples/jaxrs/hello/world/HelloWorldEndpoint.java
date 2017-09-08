package uk.me.mattgill.samples.jaxrs.hello.world;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import uk.me.mattgill.samples.jaxrs.hello.world.filter.annotations.TracedEndpoint;

@Path("/hello")
public class HelloWorldEndpoint {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @TracedEndpoint
    public String helloWorld() {
        return "Hello World!";
    }
}
