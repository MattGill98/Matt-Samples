package uk.me.mattgill.samples.message.board.post.messaging;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import uk.me.mattgill.samples.message.board.core.entity.MessageBoardPost;

@ApplicationScoped
public class Jms2MessageBoardPostReceiver {

    @Resource(lookup = "java:app/jms/MessageBoardConnectionFactory")
    private ConnectionFactory factory;

    @Resource(lookup = "java:app/jms/MessageBoardPostQueue")
    private Queue queue;

    private final List<MessageBoardPost> messages;

    public Jms2MessageBoardPostReceiver() {
        messages = new ArrayList<>();
    }

    public MessageBoardPost waitForMessage() throws JMSRuntimeException { // JMSException was replaced with unchecked JMSRuntimeException
        try (JMSContext context = factory.createContext()) {
            JMSConsumer consumer = context.createConsumer(queue);
            MessageBoardPost message = consumer.receiveBody(MessageBoardPost.class);
            messages.add(message);
            return message;
        }
    }

    public List<MessageBoardPost> getMessages() {
        return messages;
    }

    public void addMessage(MessageBoardPost message) {
        messages.add(message);
    }

}
