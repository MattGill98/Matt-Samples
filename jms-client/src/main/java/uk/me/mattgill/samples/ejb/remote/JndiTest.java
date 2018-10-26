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
import javax.naming.NamingException;

public class JndiTest {

    private static final String QUEUE_NAME = "jms/myQueue";
    private static final String CONNECTION_FACTORY_NAME = "jms/myConnFactory";

    private static final String HOST_NAME = "127.0.0.1";
    private static final String SECURE_PORT = "3820";
    private static final String TRUSTSTORE_NAME = "/opt/payara/default/appserver/glassfish/domains/domain1/config/cacerts.jks";
    private static final String TRUSTSTORE_PASSWORD = "changeit";

    public static void main(String[] args) throws Exception {

        // Print java version
        System.out.println("Java version: " + System.getProperty("java.version"));

        // Create initial context
        InitialContext ctx = createContext();
        System.out.println("Context Created");

        // JNDI Lookup for QueueConnectionFactory
        QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup(CONNECTION_FACTORY_NAME);
        System.out.println("Factory found.");

        // Create a Connection from QueueConnectionFactory
        Connection connection = factory.createConnection();
        // Initialise the communication session
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // JNDI Lookup for the Queue in remote JMS Provider
        Queue queue = (Queue) ctx.lookup(QUEUE_NAME);

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

    public static InitialContext createContext() throws NamingException {
        System.setProperty("javax.net.ssl.trustStore", TRUSTSTORE_NAME);
        System.setProperty("javax.net.ssl.trustStorePassword", TRUSTSTORE_PASSWORD);
        System.setProperty("org.omg.CORBA.ORBInitialPort", SECURE_PORT);
        System.setProperty("com.sun.CSIV2.ssl.standalone.client.required", "true");
        Properties props = new Properties();
        props.setProperty("org.omg.CORBA.ORBInitialHost", HOST_NAME);
        props.setProperty("org.omg.CORBA.ORBInitialPort", SECURE_PORT);
        // Create the initial context for remote JMS server
        InitialContext ctx = new InitialContext(props);
        return ctx;
    }

}