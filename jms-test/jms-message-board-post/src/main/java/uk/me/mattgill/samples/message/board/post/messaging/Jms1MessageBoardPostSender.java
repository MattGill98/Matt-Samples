package uk.me.mattgill.samples.message.board.post.messaging;

import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import uk.me.mattgill.samples.message.board.core.entity.MessageBoardPost;

@RequestScoped
public class Jms1MessageBoardPostSender {

    @Resource(lookup = "java:app/jms/MessageBoardConnectionFactory")
    private ConnectionFactory factory;

    @Resource(lookup = "java:app/jms/MessageBoardPostQueue")
    private Queue queue;

    public MessageBoardPost sendMessage(MessageBoardPost message) throws JMSException { // JMSException is a checked exception used in JMS 1.1
        try (Connection connection = factory.createConnection()) { // Connection first
            Session session = connection.createSession(); // Session from connection
            MessageProducer producer = session.createProducer(queue); // Producer from session
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            Message jmsMessage = session.createObjectMessage(message); // Custom message from session
            producer.send(queue, jmsMessage); // This whole spec is a ball-ache
        }
        return message;
    }
}
