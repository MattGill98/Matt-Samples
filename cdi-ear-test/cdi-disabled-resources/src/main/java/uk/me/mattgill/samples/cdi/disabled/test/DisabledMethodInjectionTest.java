package uk.me.mattgill.samples.cdi.disabled.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import uk.me.mattgill.samples.ejb.test.TestService;

@Path("/method")
public class DisabledMethodInjectionTest {

    private TestService service;

    public DisabledMethodInjectionTest() {
        try {
            InitialContext context = new InitialContext();
            service = (TestService) context.lookup("java:app/cdi-test-ejb/TestService");
        } catch (NamingException ex) {
            Logger.getLogger(DisabledConstructorInjectionTest.class.getName()).log(Level.SEVERE, "Error creating InitialContext", ex);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getMessage(@Context UriInfo info) {
        JsonArrayBuilder result = Json.createArrayBuilder();

        {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("name", getClass().getName());
            builder.add("description", "CDI disabled class with @Context in method");
            result.add(builder);
        }

        {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("name", "UriInfo");
            builder.add("from", "@Context in method");
            if (info == null) {
                builder.addNull("path");
            } else {
                builder.add("path", info.getAbsolutePath().getPath());
            }
            result.add(builder);
        }

        {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("name", "service");
            builder.add("from", "InitialContext.lookup");
            if (this.service == null) {
                builder.addNull("version");
            } else {
                builder.add("version", this.service.getVersion());
            }
            result.add(builder);
        }

        return result.build();
    }
}
