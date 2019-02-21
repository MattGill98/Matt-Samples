package uk.me.mattgill.samples.jaxrs.interceptor.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import uk.me.mattgill.samples.jaxrs.interceptor.api.TracedEndpoint;

@Path("")
public class TracedResource {

    @GET
    @TracedEndpoint
    public String helloWorld() {
        return "Hello World!";
    }

}