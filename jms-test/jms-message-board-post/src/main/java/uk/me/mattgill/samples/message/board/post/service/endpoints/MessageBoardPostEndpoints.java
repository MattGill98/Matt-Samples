package uk.me.mattgill.samples.message.board.post.service.endpoints;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.JMSRuntimeException;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import uk.me.mattgill.samples.message.board.core.entity.MessageBoardPost;
import uk.me.mattgill.samples.message.board.post.messaging.Jms2MessageBoardPostSender;

@Path("/post")
@RequestScoped
public class MessageBoardPostEndpoints {

    @Inject
    private Jms2MessageBoardPostSender sender2;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MessageBoardPost postMessage(@Valid MessageBoardPost message) {
        try {
            sender2.sendMessage(message);
        } catch (JMSRuntimeException ex) {
            Logger.getLogger(MessageBoardPostEndpoints.class.getName()).log(Level.SEVERE, "Error occurred in sending message via JMS 2.0", ex);
        }
        return message;
    }

}
