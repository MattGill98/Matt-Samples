package uk.me.mattgill.samples.grizzly;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http2.Http2AddOn;
import org.glassfish.grizzly.http2.Http2Configuration;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.grizzly.websockets.WebSocketAddOn;

import uk.me.mattgill.samples.grizzly.addon.GrizzlyAddon;

public class WebServer implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(WebServer.class.getName());

    private final HttpServer httpServer;

    private boolean running;

    /**
     * Creates a new web server.
     * 
     * @param port The port to run the insecure web server on.
     * @param securePort The port to run the secure web server on.
     * @throws IOException if there was an error reading in the keystore or truststore.
     */
    public WebServer(Integer port, Integer securePort) {
        this.httpServer = new HttpServer();
        this.running = false;

        // Configure log levels
        configureLogLevels();

        // Create web listeners
        if (port != null) {
            NetworkListener listener = createNetworkListener("http-listener", NetworkListener.DEFAULT_NETWORK_HOST, port, false);
            httpServer.addListener(listener);
        }
        if (securePort != null) {
            NetworkListener secureListener = createNetworkListener("https-listener", NetworkListener.DEFAULT_NETWORK_HOST, securePort, true);
            httpServer.addListener(secureListener);
        }
    }

    public WebServer() {
        this(9000, 9010);
    }

    public void registerAddon(GrizzlyAddon addon) {
        addon.register(httpServer);
    }

    public boolean start() {
        if (!running) {
            LOGGER.info("Web server starting...");
            try {
                httpServer.start();
                running = true;
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Web server failed to start.", ex);
            }
        } else {
            LOGGER.info("Web server already started!");
        }
        return running;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void close() {
        if (running) {
            LOGGER.info("Web server shutting down...");
            httpServer.shutdown();
            running = false;
            LOGGER.info("Web server shut down.");
        } else {
            LOGGER.info("Web server isn't running!");
        }
    }

    ///// Private methods

    private void configureLogLevels() {
        Level level = Level.FINE;
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(level);
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(level);
        }
    }

    private NetworkListener createNetworkListener(String listenerName, String host, Integer port, boolean secure) {
        NetworkListener listener = new NetworkListener(listenerName, host, port);
        listener.setSecure(false);

        if (secure) {
            try {
                SSLContextConfigurator sslContext = new SSLContextConfigurator();
                byte[] b = null;

                // Read in the keystore
                try (InputStream is = getClass().getResourceAsStream("/keystore.jks")) {
                    b = new byte[is.available()];
                    is.read(b);
                    sslContext.setKeyStoreBytes(b);
                    sslContext.setKeyStorePass("password");
                } catch (IOException ex) {
                    throw new IOException("Unable to read keystore. Making listener insecure.");
                }

                // Read in the trust store
                try (InputStream is = getClass().getResourceAsStream("/cacerts.jks")) {
                    b = new byte[is.available()];
                    is.read(b);
                    sslContext.setTrustStoreBytes(b);
                    sslContext.setTrustStorePass("password");
                } catch (IOException ex) {
                    throw new IOException("Unable to read keystore. Making listener insecure.");
                }

                // Configure SSLEngineConfigurator
                SSLEngineConfigurator configurator = new SSLEngineConfigurator(sslContext)
                        .setClientMode(false)
                        .setNeedClientAuth(false)
                        .setWantClientAuth(false);
                listener.setSecure(true);
                listener.setSSLEngineConfig(configurator);

                // Configure HTTP/2 support
                Http2Configuration config = Http2Configuration.builder().build();
                Http2AddOn http2Addon = new Http2AddOn(config);
                listener.registerAddOn(http2Addon);
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, "Error encountered while configuring secure listener. Making listener insecure.");
            }
        }

        return listener;
    }

}
