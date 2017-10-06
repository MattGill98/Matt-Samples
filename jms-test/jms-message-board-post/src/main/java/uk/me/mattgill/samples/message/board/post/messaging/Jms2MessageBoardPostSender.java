package uk.me.mattgill.samples.message.board.post.messaging;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.JMSRuntimeException;
import javax.jms.Queue;
import uk.me.mattgill.samples.message.board.core.entity.MessageBoardPost;

@RequestScoped
public class Jms2MessageBoardPostSender {

    @Resource(lookup = "java:app/jms/MessageBoardConnectionFactory")
    private ConnectionFactory factory;

    @Resource(lookup = "java:app/jms/MessageBoardPostQueue")
    private Queue queue;

    public MessageBoardPost sendMessage(MessageBoardPost message) throws JMSRuntimeException { // JMSException was replaced with unchecked JMSRuntimeException
        try (JMSContext context = factory.createContext()) {
            JMSProducer producer = context.createProducer();
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer.send(queue, message);
        }
        return message;
    }
}
