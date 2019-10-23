package uk.me.mattgill.samples.arquillian;

import static javax.ws.rs.core.Response.noContent;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/")
public class EndpointImpl implements Endpoint {

    @Override
    public Response getResponse() {
        return noContent().status(200).build();
    }

}