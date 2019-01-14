package uk.me.mattgill.samples.grizzly;

import java.io.IOException;

import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketApplication;

import uk.me.mattgill.samples.grizzly.addon.GrizzlyAddon;
import uk.me.mattgill.samples.grizzly.addon.HttpAddon;
import uk.me.mattgill.samples.grizzly.addon.WebSocketAddon;

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

            // Create WebsocketAddon
            GrizzlyAddon websocketAddon = new WebSocketAddon("/", new WebSocketApplication() {
                @Override
                public void onConnect(WebSocket socket) {
                    System.out.println(socket.toString());
                    super.onConnect(socket);
                }

                @Override
                public void onClose(WebSocket socket, DataFrame frame) {
                    System.out.println(socket.toString());
                    super.onClose(socket, frame);
                }
                @Override
                public void onMessage(WebSocket socket, String text) {
                    socket.send(text);
                    super.onMessage(socket, text);
                }
            });
            server.registerAddon(websocketAddon);

            // Start the server
            server.start();
            System.in.read();
        }
    }
}
