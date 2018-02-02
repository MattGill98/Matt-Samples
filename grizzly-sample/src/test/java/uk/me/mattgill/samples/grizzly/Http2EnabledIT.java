package uk.me.mattgill.samples.grizzly;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class Http2EnabledIT {

    private static OkHttpClient client;
    private static Request request;
    private static Request akamaiRequest;

    @BeforeAll
    public static void initialise() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
            IOException, KeyManagementException {
        X509TrustManager manager = null;
        SSLContext ctx = null;

        // 
        try (FileInputStream in = new FileInputStream("src/main/resources/cacerts.jks")) {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(in, "password".toCharArray());
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(trustStore);
            ctx = SSLContext.getInstance("TLS");
            ctx.init(null, factory.getTrustManagers(), null);
            manager = (X509TrustManager) factory.getTrustManagers()[0];
        }

        client = new OkHttpClient().newBuilder().sslSocketFactory(ctx.getSocketFactory(), manager)
                .hostnameVerifier((name, session) -> {
                    return true;
                }).build();

        akamaiRequest = new Request.Builder().url("https://http2.akamai.com/").build();
        request = new Request.Builder().url("https://localhost:9010/").build();
    }

    @Test
    public void controlGroupHttp2Test() {
        Response response = null;
        try {
            response = client.newCall(akamaiRequest).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assertTrue(response != null && response.protocol().equals(Protocol.HTTP_2),
                "Connection to " + akamaiRequest.url().toString()
                        + " wasn't over HTTP 2.0. This means the HTTP 2.0 testing mechanism is likely broken.");
    }

    @Test
    public void grizzlyHttp2Test() {
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        assertTrue(response != null && response.protocol().equals(Protocol.HTTP_2),
                "Connection to " + request.url().toString()
                        + " wasn't over HTTP 2.0. This means the HTTP 2.0 server is likely not working correctly.");
    }

}