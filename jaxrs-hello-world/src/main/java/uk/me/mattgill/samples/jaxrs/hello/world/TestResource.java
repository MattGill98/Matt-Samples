package uk.me.mattgill.samples.jaxrs.hello.world;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class TestResource {

    @GET
    public String helloWorld() {
        return "Hello World!";
    }

}