package uk.me.mattgill.samples.jaxrs.impl;

import javax.ws.rs.Path;

import uk.me.mattgill.samples.jaxrs.api.HelloWorld;

@Path("/")
public class Endpoint extends HelloWorld {

    @Override
    public String helloWorld() {
        return "Hello World!";
    }

}