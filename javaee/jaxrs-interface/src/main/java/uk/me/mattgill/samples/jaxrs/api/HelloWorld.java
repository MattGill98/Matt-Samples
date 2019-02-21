package uk.me.mattgill.samples.jaxrs.api;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;

public abstract class HelloWorld {

    @GET
    @Produces(TEXT_PLAIN)
    public abstract String helloWorld();

}