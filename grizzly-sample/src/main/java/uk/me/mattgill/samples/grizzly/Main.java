package uk.me.mattgill.samples.grizzly;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        // Configures the logging format.
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$s] %5$s\n");

        try (WebServer server = new WebServer()) {
            server.start();
            System.in.read();
        }
    }
}
