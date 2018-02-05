package uk.me.mattgill.samples.grizzly;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

@FunctionalInterface
interface FunctionalHttpHandler {

    void doService(Request request, Response response) throws Exception;

    default HttpHandler convert() {
        return new HttpHandler() {
            @Override
            public void service(Request request, Response response) throws Exception {
                doService(request, response);
            }
        };
    }

}