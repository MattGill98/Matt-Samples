package uk.me.mattgill.samples.grizzly;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args)
            throws InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        // Configures the logging format.
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$s] %5$s\n");

        // Creates a new web server
        try (WebServer server = new WebServer()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(server::start);
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        }
    }
}
