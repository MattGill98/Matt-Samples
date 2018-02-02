package uk.me.mattgill.samples.grizzly;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class Http2EnabledTest {

    private static WebServer server;

    private static OkHttpClient client;
    private Request request;
    private Request secureRequest;

    @BeforeAll
    public static void initialise() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
            IOException, KeyManagementException {
        server = new WebServer(Level.OFF);

        X509TrustManager manager = null;
        SSLContext ctx = null;

        try (FileInputStream in = new FileInputStream("src/main/resources/cacerts.jks")) {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(in, "password".toCharArray());
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(trustStore);
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, factory.getTrustManagers(), null);
            manager = (X509TrustManager) factory.getTrustManagers()[0];
        }

        client = new OkHttpClient().newBuilder()
            .sslSocketFactory(ctx.getSocketFactory(), manager)
            .hostnameVerifier((name, session) -> {return true;})
            .build();
    }

    @BeforeEach
    public void startServer() {
        try {
            server.call();
        } catch (NoClassDefFoundError ex) {
            throw new AssertionError(
                    "The ALPN files weren't found. This means the ALPN jar wasn't on the boot class path.", ex);
        }
        assertTrue(server.isRunning(), "Server didn't start up correctly.");

        request = new Request.Builder().url(server.getEndpointUrl()).build();
        secureRequest = new Request.Builder().url(server.getSecureEndpointUrl()).build();
    }

    @AfterEach
    public void stopServer() {
        server.close();
        assertFalse(server.isRunning(), "Server didn't shut down correctly.");
    }

    @Test
    public void servesHttp2() throws IOException {
        Response response = client.newCall(secureRequest).execute();

        String responseJson = String.format(
                "{\n" + "    Message: \"%s\",\n" + "    Protocol: \"%s\",\n" + "    Handshake: \"%s\"\n" + "}",
                response.message(), response.protocol().toString(),
                (response.handshake() == null) ? "Insecure" : response.handshake().cipherSuite().javaName());

        assertTrue(response.protocol().equals(Protocol.HTTP_2),
                "Response wasn't over HTTP 2.0.\nResponse: " + responseJson);
    }

}