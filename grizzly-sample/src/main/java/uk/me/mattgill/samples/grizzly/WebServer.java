package uk.me.mattgill.samples.grizzly;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http2.Http2AddOn;
import org.glassfish.grizzly.http2.Http2Configuration;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;

public class WebServer implements Callable<Boolean>, AutoCloseable {

    private final Logger logger = Logger.getLogger(WebServer.class.getName());

    private final int port;
    private final int securePort;

    private final HttpServer httpServer;
    private boolean running;

    private String endpointUrl;
    private String secureEndpointUrl;

    /**
     * Creates a new web server.
     * 
     * @param port The port to run the insecure web server on.
     * @param securePort The port to run the secure web server on.
     * @param logLevel the level of logging for the server. Level.OFF will disable logging.
     */
    public WebServer(int port, int securePort, Level logLevel) {
        this.port = port;
        this.securePort = securePort;
        this.httpServer = new HttpServer();
        this.running = false;
        this.endpointUrl = null;
        this.secureEndpointUrl = null;

        // Configure log levels
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(logLevel);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(logLevel);
        }
    }

    /**
     * Creates a new web server. Defaults the log level to Level.INFO.
     */
    public WebServer(int port, int securePort) {
        this(port, securePort, Level.INFO);
    }

    public WebServer(Level logLevel) {
        this(9000, 9010, logLevel);
    }

    public WebServer() {
        this(Level.INFO);
    }

    @Override
    public Boolean call() {
        if (running) {
            logger.info("Web server already started!");
            return running;
        }
        logger.info("Web server starting...");

        // Create web listeners
        NetworkListener listener = new NetworkListener("http-listener", NetworkListener.DEFAULT_NETWORK_HOST, port);
        NetworkListener secureListener = new NetworkListener("https-listener", NetworkListener.DEFAULT_NETWORK_HOST,
                securePort);

        // Configure http-listener
        listener.setSecure(false);

        // Configure https-listener
        secureListener.setSecure(true);
        SSLContextConfigurator sslContext = new SSLContextConfigurator();
        sslContext.setKeyStoreFile("src/main/resources/keystore.jks");
        sslContext.setTrustStoreFile("src/main/resources/cacerts.jks");
        sslContext.setKeyStorePass("password");
        sslContext.setTrustStorePass("password");
        secureListener.setSSLEngineConfig(
                new SSLEngineConfigurator(sslContext).setClientMode(false).setNeedClientAuth(false));

        // Configure HTTP/2 support
        Http2Configuration config = Http2Configuration.builder().build();
        Http2AddOn http2Addon = new Http2AddOn(config);
        secureListener.registerAddOn(http2Addon);

        // Add listeners and handler
        httpServer.addListener(listener);
        httpServer.addListener(secureListener);
        httpServer.getServerConfiguration().addHttpHandler(new CustomHttpHandler(), "/");

        // Start the server
        try {
            httpServer.start();
        } catch (IOException e) {
            logger.warning("Web server failed to start.");
            return running;
        }
        this.endpointUrl = String.format("http://%s:%d/", InetAddress.getLoopbackAddress().getHostAddress(), port);
        this.secureEndpointUrl = String.format("https://%s:%d/", InetAddress.getLoopbackAddress().getHostAddress(),
                securePort);
        return running = true;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public String getSecureEndpointUrl() {
        return secureEndpointUrl;
    }

    public boolean isRunning() {
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

    class CustomHttpHandler extends HttpHandler {

        @Override
        public void service(Request request, Response response) throws Exception {
            response.setContentType("text/plain");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write("Hello World!");
        }

    }

}
