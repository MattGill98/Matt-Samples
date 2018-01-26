package uk.me.mattgill.samples.grizzly.test;

import java.io.IOException;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http2.Http2AddOn;
import org.glassfish.grizzly.http2.Http2Configuration;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;

public class WebContainer {

    private static final Logger logger = Logger.getLogger(WebContainer.class.getName());

    private final HttpServer httpServer;

    public WebContainer() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stop();
        }));
        httpServer = new HttpServer();
    }

    public void start(int port, int securePort) throws IOException, InterruptedException {
        NetworkListener listener = new NetworkListener("http-listener", NetworkListener.DEFAULT_NETWORK_HOST, port);
        NetworkListener secureListener = new NetworkListener("secure-http-listener", NetworkListener.DEFAULT_NETWORK_HOST, securePort);

        // Configure listener
        listener.setSecure(false);

        // Configure secure listener
        secureListener.setSecure(true);
        SSLContextConfigurator sslContext = new SSLContextConfigurator();
        sslContext.setKeyStoreFile("src/main/resources/keystore.jks");
        sslContext.setKeyStorePass("changeit");
        secureListener.setSSLEngineConfig(new SSLEngineConfigurator(sslContext)
                .setClientMode(false)
                .setNeedClientAuth(false));

        // Configure HTTP/2 support
        Http2Configuration config = Http2Configuration.builder().build();
        Http2AddOn http2Addon = new Http2AddOn(config);
        secureListener.registerAddOn(http2Addon);

        // Add listeners and handler
        httpServer.addListener(listener);
        httpServer.addListener(secureListener);
        httpServer.getServerConfiguration().addHttpHandler(new MainHttpHandler(), "/");
        httpServer.start();
        logger.info("Press CTRL+C to stop the server.");
        Thread.currentThread().join();
    }

    public void stop() {
        logger.info("Shutting down server.");
        httpServer.shutdown();
    }

}
