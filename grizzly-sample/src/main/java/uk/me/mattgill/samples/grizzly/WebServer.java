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

public class WebServer implements AutoCloseable {

    private final Logger logger = Logger.getLogger(WebServer.class.getName());

    private final HttpServer httpServer;

    private boolean running;

    public boolean start() {
        if (!running) {
            logger.info("Web server starting...");
            try {
                httpServer.start();
                running = true;
            } catch (IOException e) {
                logger.warning("Web server failed to start.");
            }
        } else {
            logger.info("Web server already started!");
        }
        return running;
    }

    @Override
    public void close() {
        if (running) {
            logger.info("Web server shutting down...");
            httpServer.shutdown();
            running = false;
            logger.info("Web server shut down.");
        } else {
            logger.info("Web server isn't running!");
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setLogLevel(Level level) {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(level);
        for (Handler h : rootLogger.getHandlers()) {
            h.setLevel(level);
        }
    }

    private void configureSecureListener(NetworkListener listener) throws IOException {
        listener.setSecure(true);
        SSLContextConfigurator sslContext = new SSLContextConfigurator();
        byte[] b = null;
        // Read in the keystore
        try (InputStream is = getClass().getResourceAsStream("/keystore.jks")) {
            b = new byte[is.available()];
            is.read(b);
            sslContext.setKeyStoreBytes(b);
            sslContext.setKeyStorePass("password");
        }
        // Read in the trust store
        try (InputStream is = getClass().getResourceAsStream("/cacerts.jks")) {
            b = new byte[is.available()];
            is.read(b);
            sslContext.setTrustStoreBytes(b);
            sslContext.setTrustStorePass("password");
        }
        SSLEngineConfigurator configurator = new SSLEngineConfigurator(sslContext)
                .setClientMode(false)
                .setNeedClientAuth(false)
                .setWantClientAuth(true);
        listener.setSSLEngineConfig(configurator);
    }

    /**
     * Creates a new web server.
     * 
     * @param port The port to run the insecure web server on.
     * @param securePort The port to run the secure web server on.
     * @param logLevel the level of logging for the server. Level.OFF will disable logging.
     * @param handler the handler to assign to the root of the server. Defines what to do on a request.
     * @throws IOException if there was an error reading in the keystore or truststore.
     */
    public WebServer(int port, int securePort, Level logLevel, FunctionalHttpHandler handler) {
        this.httpServer = new HttpServer();
        assert (handler != null);
        this.running = false;

        // Configure log levels
        setLogLevel(logLevel);

        // Create web listeners
        NetworkListener listener = new NetworkListener("http-listener", NetworkListener.DEFAULT_NETWORK_HOST, port, 3);
        NetworkListener secureListener = new NetworkListener("https-listener", NetworkListener.DEFAULT_NETWORK_HOST,
                securePort);

        // Configure http-listener
        listener.setSecure(false);

        // Configure https-listener
        try {
			configureSecureListener(secureListener);
		} catch (IOException ex) {
            logger.log(Level.WARNING, "Secure listener could not be configured, so it was made insecure instead.", ex);
            secureListener.setSecure(false);
		}

        // Configure HTTP/2 support
        Http2Configuration config = Http2Configuration.builder().build();
        Http2AddOn http2Addon = new Http2AddOn(config);
        listener.registerAddOn(http2Addon);
        secureListener.registerAddOn(http2Addon);

        // Add listeners and handler
        httpServer.addListener(listener);
        httpServer.addListener(secureListener);
        httpServer.getServerConfiguration().addHttpHandler(handler.convert(), "/");
    }

    public WebServer(int port, int securePort, Level level) {
        this(port, securePort, level, (request, response) -> {
            response.setContentType("text/plain");
            response.getWriter().write("Hello World!");
        });
    }

    public WebServer(Level level) {
        this(9000, 9010, level);
    }

}
