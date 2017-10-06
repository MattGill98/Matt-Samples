package uk.me.mattgill.samples.message.board.post.service.endpoints;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import uk.me.mattgill.samples.message.board.post.messaging.Jms2MessageBoardPostReceiver;
import uk.me.mattgill.samples.message.board.core.entity.MessageBoardPost;

@Path("/receive")
@RequestScoped
public class MessageBoardReceiveEndpoints {

    @Inject
    private Jms2MessageBoardPostReceiver receiver2;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MessageBoardPost waitForMessageNew() {
        try {
            return receiver2.waitForMessage();
        } catch (JMSRuntimeException ex) {
            Logger.getLogger(MessageBoardReceiveEndpoints.class.getName()).log(Level.SEVERE, "Error occurred in receiving message via JMS 2.0", ex);
        }
        return null;
    }

    @GET
    @Path("/all")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageBoardPost> getAllMessagesNew() {
        return receiver2.getMessages();
    }

}
