package uk.me.mattgill.samples.cluster.ping.service.resource;

import com.hazelcast.logging.Logger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

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
import uk.me.mattgill.samples.cluster.ping.event.management.MessageReceiver;

@Path("/")
@RequestScoped
public class TrackerResource {

    @Inject
    private MessageReceiver receiver;

   

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
        TrackerMessage message = new TrackerMessage(messageText);

        try {
            // Wait for it to return, and return the contents.
            return receiver.get(message, 5, TimeUnit.SECONDS).toString();
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            Logger.getLogger("cluster-ping").log(Level.SEVERE, "Error handling request for " + message.getId());
            throw new WebApplicationException(Response.serverError().entity(e.getMessage()).build());
        }
    }

}
