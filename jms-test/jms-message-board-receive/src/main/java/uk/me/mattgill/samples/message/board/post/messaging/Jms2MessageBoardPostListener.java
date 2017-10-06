package uk.me.mattgill.samples.message.board.post.messaging;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import uk.me.mattgill.samples.message.board.core.entity.MessageBoardPost;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:global/jms/MessageBoardPostQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "java:global/jms/MessageBoardConnectionFactory")
})
public class Jms2MessageBoardPostListener implements MessageListener {
    
    @Inject
    private Jms2MessageBoardPostReceiver receiver;

    @Override
    public void onMessage(Message message) {
        try {
            MessageBoardPost receivedMessage = message.getBody(MessageBoardPost.class);
            receiver.addMessage(receivedMessage);
        } catch (JMSException ex) {
            // incorrect data type, but don't worry everyt'ing gon' be irie
        }
    }
    
}
