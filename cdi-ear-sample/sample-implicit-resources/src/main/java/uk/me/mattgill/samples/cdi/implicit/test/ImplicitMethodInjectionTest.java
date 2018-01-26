package uk.me.mattgill.samples.cdi.implicit.test;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import uk.me.mattgill.samples.ejb.test.TestService;

@Path("/method")
public class ImplicitMethodInjectionTest {

    @EJB
    private TestService ejbService;

    @Inject
    private TestService cdiService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray getMessage(@Context UriInfo info) {
        JsonArrayBuilder result = Json.createArrayBuilder();

        {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("name", getClass().getName());
            builder.add("description", "CDI implicit class with @Context in method");
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
            builder.add("name", "ejbService");
            builder.add("from", "@EJB in field");
            if (this.ejbService == null) {
                builder.addNull("version");
            } else {
                builder.add("version", this.ejbService.getVersion());
            }
            result.add(builder);
        }

        {
            JsonObjectBuilder builder = Json.createObjectBuilder();
            builder.add("name", "cdiService");
            builder.add("from", "@Inject in field");
            if (this.cdiService == null) {
                builder.addNull("version");
            } else {
                builder.add("version", this.cdiService.getVersion());
            }
            result.add(builder);
        }

        return result.build();
    }
}
