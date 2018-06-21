package uk.me.mattgill.samples.jaxrs.interceptor.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import uk.me.mattgill.samples.jaxrs.interceptor.api.TracedEndpoint;

@TracedEndpoint
@Provider
@ApplicationScoped
public class TracedEndpointFilter implements ContainerRequestFilter {

    private Logger logger = Logger.getLogger(TracedEndpointFilter.class.getName());

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.info(String.format("(%s) %s", requestContext.getMethod(),
                requestContext.getUriInfo().getAbsolutePath().toString()));
    }

}
