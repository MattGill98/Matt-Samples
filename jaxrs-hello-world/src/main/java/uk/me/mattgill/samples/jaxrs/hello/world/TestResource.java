package uk.me.mattgill.samples.jaxrs.hello.world;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
@RequestScoped
public class TestResource {

    @GET
    public String helloWorld() {
        return "Hello World!";
    }

}
