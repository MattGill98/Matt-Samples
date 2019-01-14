package uk.me.mattgill.samples.grizzly.addon;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.websockets.DataFrame;
import org.glassfish.grizzly.websockets.WebSocket;
import org.glassfish.grizzly.websockets.WebSocketAddOn;
import org.glassfish.grizzly.websockets.WebSocketApplication;
import org.glassfish.grizzly.websockets.WebSocketEngine;

public class WebSocketAddon implements GrizzlyAddon {

    private final String contextPath;
    private final String urlPattern;

    private final WebSocketAddOn addon;
    private final WebSocketApplication application;

    public WebSocketAddon(String urlPattern, WebSocketApplication application) {
        this("", urlPattern, application);
    }

    public WebSocketAddon(String contextPath, String urlPattern, WebSocketApplication application) {
        this.contextPath = contextPath;
        this.urlPattern = urlPattern;
        this.application = application;
        this.addon = new WebSocketAddOn();
    }

    @Override
    public void register(HttpServer httpServer) {

        // Register addon with all listeners
        httpServer.getListeners().forEach(listener -> listener.registerAddOn(addon));

        // Register websocket application
        WebSocketEngine.getEngine().register(contextPath, urlPattern, application);
    }

}