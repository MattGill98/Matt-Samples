package uk.me.mattgill.samples.jaxrs.hello.world.filter;

import java.io.IOException;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.enterprise.context.ApplicationScoped;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import uk.me.mattgill.samples.jaxrs.hello.world.api.TracedEndpoint;

@TracedEndpoint
@Provider
@ApplicationScoped
public class TracedEndpointFilter implements ContainerRequestFilter {

    private Logger logger = Logger.getLogger(TracedEndpointFilter.class.getName());

    @Inject
    @ConfigProperty(name = "tracing-enabled")
    private boolean tracingEnabled;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (tracingEnabled) {
            logger.info(String.format("(%s) %s", requestContext.getMethod(),
                    requestContext.getUriInfo().getAbsolutePath().toString()));
        }
    }

}
