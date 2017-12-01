package uk.me.mattgill.samples.jaxrs.hello.world.filter;

import uk.me.mattgill.samples.jaxrs.hello.world.api.TracedEndpoint;
import java.io.IOException;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

@TracedEndpoint
@Provider
@RequestScoped
public class TracedEndpointFilter implements ContainerRequestFilter {

    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.info(String.format("(%s) %s", requestContext.getMethod(), requestContext.getUriInfo().getAbsolutePath().toString()));
    }

}
