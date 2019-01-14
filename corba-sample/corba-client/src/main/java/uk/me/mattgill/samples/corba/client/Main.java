package uk.me.mattgill.samples.corba.client;

import java.util.Properties;

import javax.naming.InitialContext;

import uk.me.mattgill.samples.corba.common.LogResult;
import uk.me.mattgill.samples.corba.common.Logger;

public class Main {

    public static void run(String host, String port, String message) {
        // Attempt to lookup EJB using JNDI
        try {
            Properties props = new Properties();
            props.setProperty("org.omg.CORBA.ORBInitialHost", host);
            props.setProperty("org.omg.CORBA.ORBInitialPort", port);
            props.setProperty("java.naming.factory.initial", "com.sun.enterprise.naming.SerialInitContextFactory"); 
            props.setProperty("java.naming.factory.url.pkgs", "com.sun.enterprise.naming"); 
            props.setProperty("java.naming.factory.state", "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            Object service = new InitialContext(props).lookup("java:global/app/LoggerService");
            LogResult result = Logger.class.cast(service).log(message);

            System.out.println(result.toString());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String message = "";
            for (int i = 0; i < args.length; i++) {
                message += args[i] + " ";
            }
            message = message.trim();

            if (message.isEmpty()) {
                message = "Hello World!";
            }

            Main.run("127.0.0.1", "3700", message);

        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
}
