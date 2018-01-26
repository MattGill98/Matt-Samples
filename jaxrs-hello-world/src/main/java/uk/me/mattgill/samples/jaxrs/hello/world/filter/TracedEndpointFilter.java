package uk.me.mattgill.samples.jaxrs.hello.world.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import uk.me.mattgill.samples.jaxrs.hello.world.api.TracedEndpoint;

@TracedEndpoint
@Provider
@RequestScoped
public class TracedEndpointFilter implements ContainerRequestFilter {

    private Logger logger = Logger.getLogger(TracedEndpointFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.info(String.format("(%s) %s", requestContext.getMethod(), requestContext.getUriInfo().getAbsolutePath().toString()));
    }

}
