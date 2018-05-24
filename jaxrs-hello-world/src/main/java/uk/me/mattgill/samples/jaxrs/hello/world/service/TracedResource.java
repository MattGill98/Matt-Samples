package uk.me.mattgill.samples.jaxrs.hello.world.service;

import javax.ws.rs.Path;
import javax.ws.rs.GET;

import uk.me.mattgill.samples.jaxrs.hello.world.api.TracedEndpoint;

@Path("")
public class TracedResource {

    @GET
    @TracedEndpoint
    public String helloWorld() {
        return "Hello World!";
    }

}