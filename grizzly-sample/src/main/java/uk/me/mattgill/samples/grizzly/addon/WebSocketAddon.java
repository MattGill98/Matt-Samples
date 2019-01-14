package uk.me.mattgill.samples.grizzly.addon;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;

public class WebSocketAddon implements GrizzlyAddon {

    private final String contextPath;
    private final String urlPattern;

    private final WebSocketAddOn addon;
    private final FunctionalWebSocketApplication application;

    public WebSocketAddon(String urlPattern, FunctionalWebSocketApplication application) {
        this("", urlPattern, application);
    }

    public WebSocketAddon(String contextPath, String urlPattern, FunctionalWebSocketApplication application) {
        this.contextPath = contextPath;
        this.urlPattern = urlPattern;
        this.application = application;
        this.addon = new WebSocketAddOn();
    }

    @Override
    public void register(HttpServer httpServer) {

        // Register addon with all listeners
        httpServer.getListeners().forEach(listener -> {
            listener.setMaxPendingBytes(-1);
            listener.registerAddOn(addon);
        });

        // Register websocket application
        WebSocketEngine.getEngine().register(contextPath, urlPattern, application.convert());
    }

    @FunctionalInterface
    public interface FunctionalWebSocketApplication {

        void handleMessage(WebSocket socket, String text) ;

        default WebSocketApplication convert() {
            return new WebSocketApplication() {
                @Override
                public void onMessage(WebSocket socket, String text) {
                    handleMessage(socket, text);
                }
            };
        }

    }

}