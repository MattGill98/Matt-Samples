package uk.me.mattgill.samples.grizzly.test;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;

public class MainHttpHandler extends HttpHandler {

    @Override
    public void service(Request request, Response response) throws Exception {
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("Hello World!");
    }

}
