package uk.me.mattgill.samples.grizzly.addon;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class HttpAddon implements GrizzlyAddon {

    @FunctionalInterface
    public interface FunctionalHttpHandler {

        void doService(Request request, Response response) throws Exception;

        default HttpHandler convert() {
            return new HttpHandler() {
                @Override
                public void service(Request request, Response response) throws Exception {
                    response.setCharacterEncoding(UTF_8.name());
                    doService(request, response);
                }
            };
        }

    }

    private String path;
    private FunctionalHttpHandler handler;

    public HttpAddon(String path, FunctionalHttpHandler handler) {
        this.handler = handler;
    }

    @Override
    public void register(HttpServer httpServer) {
        httpServer.getServerConfiguration().addHttpHandler(handler.convert(), path);
    }

}