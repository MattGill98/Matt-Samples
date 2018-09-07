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

    private static final String VAR_NAME = "custom.var";

    @Inject
    @ConfigProperty(name = VAR_NAME, defaultValue = "UNKNOWN")
    private String var;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld() {
        return VAR_NAME + " = " + var;
    }
}
