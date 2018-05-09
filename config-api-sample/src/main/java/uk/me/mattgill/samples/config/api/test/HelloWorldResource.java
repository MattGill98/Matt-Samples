package uk.me.mattgill.samples.config.api.test;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/")
@RequestScoped
public class HelloWorldResource {

    @Inject
    @ConfigProperty(name = "config.custom.var", defaultValue = "UNKNOWN")
    private String var;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return "var = " + var;
    }
}
