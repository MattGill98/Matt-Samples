package uk.me.mattgill.samples.grizzly;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.glassfish.grizzly.Connection;
import org.glassfish.grizzly.filterchain.BaseFilter;
import org.glassfish.grizzly.filterchain.FilterChain;
import org.glassfish.grizzly.filterchain.FilterChainBuilder;
import org.glassfish.grizzly.filterchain.FilterChainContext;
import org.glassfish.grizzly.filterchain.NextAction;
import org.glassfish.grizzly.filterchain.TransportFilter;
import org.glassfish.grizzly.http.HttpClientFilter;
import org.glassfish.grizzly.http.HttpContent;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http2.Http2ClientFilter;
import org.glassfish.grizzly.http2.Http2Configuration;
import org.glassfish.grizzly.nio.transport.TCPNIOTransport;
import org.glassfish.grizzly.nio.transport.TCPNIOTransportBuilder;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.grizzly.ssl.SSLFilter;

public class Http2Client implements AutoCloseable {

    private Connection<HttpContent> conn;
    private CompletableFuture<HttpContent> result;

    /**
     * Creates a new HTTP client.
     * 
     * @param host the host to connect to.
     * @param port the port of the host to connect to.
     * @param timeout the amount of time to wait for the connection.
     * @param unit the unit of time to wait for the connection.
     * 
     * @throws IOException if an error is thrown while reading the trust store.
     * @throws InterruptedException if the thread is interrupted while connecting.
     * @throws ExecutionException if an error is thrown while connecting.
     * @throws TimeoutException if the connection timed out.
     */
    public Http2Client(String host, int port, long timeout, TimeUnit unit)
            throws IOException, InterruptedException, ExecutionException, TimeoutException {
        conn = null;
        result = new CompletableFuture<>();

        // Configure filter chain
        FilterChain clientChain = FilterChainBuilder.stateless().add(new TransportFilter())
                .add(new SSLFilter(null, getClientSSLEngineConfigurator())).add(new HttpClientFilter())
                .add(new Http2ClientFilter(Http2Configuration.builder().priorKnowledge(true).build()))
                .add(new BaseFilter() {
                    @Override
                    public NextAction handleRead(FilterChainContext ctx) throws IOException {
                        result.complete(ctx.getMessage());
                        return ctx.getStopAction();
                    }
                }).build();

        final TCPNIOTransport transport = TCPNIOTransportBuilder.newInstance().setProcessor(clientChain).build();
        try {
            transport.start();
        } catch (IOException e) {
            throw new ExecutionException(e);
        }

        conn = transport.connect(host, port).get(timeout, unit);
        assertTrue(conn != null, "The connection was null.");
    }

    /**
     * Makes a GET request.
     * 
     * @param request the request to make.
     * @param timeout the amount of time to wait for a response.
     * @param unit the unit of time to wait for a response.
     * 
     * @throws InterruptedException if the thread is interrupted while making the request.
     * @throws ExecutionException if an error is thrown while making the request.
     * @throws TimeoutException if the request timed out.
     */
    public HttpContent get(HttpRequestPacket request, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        result = new CompletableFuture<>();
        conn.write(request);
        return result.get(timeout, unit);
    }

    private SSLEngineConfigurator getClientSSLEngineConfigurator() throws IOException {
        SSLContextConfigurator sslContext = new SSLContextConfigurator();
        byte[] b = null;
        // Read in the trust store
        try (InputStream is = getClass().getResourceAsStream("/cacerts.jks")) {
            b = new byte[is.available()];
            is.read(b);
            sslContext.setTrustStoreBytes(b);
            sslContext.setTrustStorePass("password");
        }
        return new SSLEngineConfigurator(sslContext).setClientMode(true).setNeedClientAuth(false)
                .setWantClientAuth(false)
                .setEnabledCipherSuites(new String[] { "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256" });
    }

    @Override
    public void close() throws Exception {
        conn.getTransport().shutdownNow();
    }

}