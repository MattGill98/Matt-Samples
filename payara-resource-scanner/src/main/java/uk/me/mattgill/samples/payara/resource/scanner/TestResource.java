package uk.me.mattgill.samples.payara.resource.scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.enterprise.config.serverbeans.Domain;

import org.glassfish.internal.api.Globals;
import org.glassfish.jdbc.config.JdbcConnectionPool;
import org.glassfish.jdbc.config.JdbcResource;

@Path("/")
public class TestResource {

    private static final Domain domain = Globals.getDefaultBaseServiceLocator().getService(Domain.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonArray helloWorld() throws ClassNotFoundException {
        JsonArrayBuilder builder = Json.createArrayBuilder();

        // For each resource in the domain
        for (JdbcResource resource : domain.getResources().getResources(JdbcResource.class)) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

            objectBuilder.add("Resource Name", resource.getJndiName());
            objectBuilder.add("Pool Name", resource.getPoolName());

            // Search for the pool, and verify it exists
            JdbcConnectionPool pool = (JdbcConnectionPool) domain.getResources().getResourceByName(JdbcConnectionPool.class, resource.getPoolName());
            objectBuilder.add("Pool Exists", pool != null);

            builder.add(objectBuilder.build());
        }
        return builder.build();
    }

}
