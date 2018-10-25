package uk.me.mattgill.samples.ejb.remote;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JndiPublisher implements Runnable, AutoCloseable {

    private MessageProducer producer;
    private final TextMessage message;

    public JndiPublisher(Session session, Queue queue) throws JMSException {
        this.producer = session.createProducer(queue);

        this.message = session.createTextMessage();
        this.message.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
        this.message.setText("Hello from Arquillian!");
    }

    @Override
    public void run() {
        try {
            producer.send(message);
            System.out.println("Message Sent.");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        producer.close();
    }

}