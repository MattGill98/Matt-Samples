package uk.me.mattgill.samples.grizzly;

import java.io.IOException;

import uk.me.mattgill.samples.grizzly.addon.GrizzlyAddon;
import uk.me.mattgill.samples.grizzly.addon.HttpAddon;

public class Main {

    public static void main(String[] args) throws IOException {

        // Configures the logging format.
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$s] %5$s\n");

        try (WebServer server = new WebServer()) {

            // Create HttpAddon
            GrizzlyAddon httpAddon = new HttpAddon("/", (request, response) -> {
                response.setContentType("text/plain");
                response.getWriter().write("Hello World!");
            });
            server.registerAddon(httpAddon);

            // Start the server
            server.start();
            System.in.read();
        }
    }
}
