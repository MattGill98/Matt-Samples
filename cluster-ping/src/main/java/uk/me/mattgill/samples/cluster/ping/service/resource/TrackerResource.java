package uk.me.mattgill.samples.cluster.ping.service.resource;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import uk.me.mattgill.samples.cluster.ping.event.entity.TrackerMessage;
import uk.me.mattgill.samples.cluster.ping.event.hazelcast.RouteGenerator;
import uk.me.mattgill.samples.cluster.ping.event.management.MessageRouter;

@Path("/")
@RequestScoped
public class TrackerResource {

    @Inject
    private MessageRouter router;

    @Inject
    private RouteGenerator routeGenerator;

    /**
     * Endpoint for sending a message across the cluster.
     * 
     * @param messageText the text to send in the message. Defaults to 'Hello World!'.
     * 
     * @return the final contents of the message.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWorld(@QueryParam("message") String messageText) {
        if (messageText == null || messageText.isEmpty()) {
            messageText = "Hello World!";
        }
        TrackerMessage message = new TrackerMessage(messageText, routeGenerator.getRoute());

        try {
            // Wait for it to return, and return the contents.
            return router.route(message, 5, TimeUnit.SECONDS).toString();
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
        }
    }

}
