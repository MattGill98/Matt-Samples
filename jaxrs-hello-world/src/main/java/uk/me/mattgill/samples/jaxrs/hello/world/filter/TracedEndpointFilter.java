package uk.me.mattgill.samples.jaxrs.hello.world.filter;

import uk.me.mattgill.samples.jaxrs.hello.world.api.TracedEndpoint;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import org.slf4j.Logger;

@TracedEndpoint
@Provider
@RequestScoped
public class TracedEndpointFilter implements ContainerRequestFilter {

    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        logger.info("({}) {}", requestContext.getMethod(), requestContext.getUriInfo().getAbsolutePath().toString());
    }

}
