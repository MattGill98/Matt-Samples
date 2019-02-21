package uk.me.mattgill.samples.ejb.remote;

import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.naming.InitialContext;

public class JndiTest {

    public static void main(String[] args) throws Exception {

        // Load properties
        Properties connectionProperties = new Properties();
        connectionProperties.load(JndiTest.class.getClassLoader().getResourceAsStream("connection.properties"));
        System.getProperties().putAll(connectionProperties);

        // Print java version
        System.out.println("Java version: " + System.getProperty("java.version"));

        // Create initial context
        InitialContext ctx = new InitialContext(connectionProperties);
        System.out.println("Context Created");

        // JNDI Lookup for QueueConnectionFactory
        QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup(connectionProperties.getProperty("CONNECTION_FACTORY_NAME"));
        System.out.println("Factory found.");

        // Create a Connection from QueueConnectionFactory
        Connection connection = factory.createConnection();
        // Initialise the communication session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // JNDI Lookup for the Queue in remote JMS Provider
        Queue queue = (Queue) ctx.lookup(connectionProperties.getProperty("QUEUE_NAME"));

        // Receive messages
        MessageConsumer mc = session.createConsumer(queue);
        connection.start();
        mc.setMessageListener(new JndiListener());

        // Start scheduling messages to send
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        JndiPublisher publisher = new JndiPublisher(session, queue);
        executor.scheduleAtFixedRate(publisher, 1, 1, TimeUnit.SECONDS);

        // Make sure all the resources are released
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down.");
            System.out.flush();
            executor.shutdownNow();
            try {
                publisher.close();
                session.close();
                ctx.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.out.println("Shutdown complete.");
            }
        }));
    }

}