package uk.me.mattgill.samples.grizzly;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import org.glassfish.grizzly.http.HttpContent;
import org.glassfish.grizzly.http.HttpRequestPacket;
import org.glassfish.grizzly.http.HttpResponsePacket;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.Protocol;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Http2EnabledIT {

    private static WebServer server;

    @BeforeAll
    public static void configureServer() throws InterruptedException {
        server = new WebServer(9000, 9010, Level.INFO, (request, response) -> {
            response.setContentType("text/plain");
            response.getWriter().write("Hello Test!");
        });
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        server.close();
    }

    @Test
    public void grizzlyHttp2Test() {
        final HttpRequestPacket request = HttpRequestPacket.builder()
                .uri("/")
                .host("localhost")
                .method(Method.GET)
                .build();

        try (Http2Client client = new Http2Client("localhost", 9010, true, 1, TimeUnit.SECONDS)) {
            HttpContent response = client.get(request, 1, TimeUnit.SECONDS);
            HttpResponsePacket responsePacket = (HttpResponsePacket) response.getHttpHeader();

            assertTrue(responsePacket.getStatus() == 200, "Request status was not 200.");
            assertTrue(response.getHttpHeader().getProtocol().equals(Protocol.HTTP_2_0),
                    "Response wasn't sent over HTTP 2.");
        } catch (IOException ex) {
            throw new AssertionError("An exception was thrown while reading the trust store.", ex);
        } catch (InterruptedException ex) {
            throw new AssertionError("The thread was interrupted while connecting.", ex);
        } catch (ExecutionException ex) {
            throw new AssertionError("The connection failed.", ex);
        } catch (TimeoutException ex) {
            throw new AssertionError("The connection timed out.", ex);
        } catch (Exception ex) {
            throw new AssertionError("An unexpected exception was thrown.", ex);
        }
    }

}