package uk.me.mattgill.samples.grizzly.websocket;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.providers.grizzly.GrizzlyAsyncHttpProvider;
import com.ning.http.client.ws.DefaultWebSocketListener;
import com.ning.http.client.ws.WebSocket;
import com.ning.http.client.ws.WebSocketListener;
import com.ning.http.client.ws.WebSocketUpgradeHandler;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import uk.me.mattgill.samples.grizzly.WebServer;
import uk.me.mattgill.samples.grizzly.addon.WebSocketAddon;

public class WebSocketIT {

    private static WebServer server;
    private static String websocketUrl;
    private static AsyncHttpClient client;

    @BeforeAll
    public static void configureServer() throws InterruptedException {
        server = new WebServer(9000, 9010);
        websocketUrl = "ws://localhost:9000";
        server.registerAddon(new WebSocketAddon("/", (socket, text) -> {
            socket.send(new StringBuilder(text).reverse().toString());
        }));
        server.start();

        AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().build();
        client = new AsyncHttpClient(new GrizzlyAsyncHttpProvider(config));
    }

    @AfterAll
    public static void stopServer() {
        server.close();
    }

    @Test
    public void webSocketTest() throws InterruptedException, ExecutionException, UnsupportedEncodingException {
        CountDownLatch latch = new CountDownLatch(1);
        // This example will send a WS message to a server that echoes the message back
        WebSocketListener listener = new DefaultWebSocketListener() {
            @Override
            public void onMessage(String message) {
                assertEquals("!olleH", message);
                latch.countDown();
            }
        };
        WebSocketUpgradeHandler handler = new WebSocketUpgradeHandler.Builder().addWebSocketListener(listener).build();
        WebSocket socket = client.prepareGet(websocketUrl).execute(handler).get();
        socket.sendMessage("Hello!");
        if (!latch.await(1, TimeUnit.SECONDS)) {
            fail("No response from websocket.");
        }
    }

}