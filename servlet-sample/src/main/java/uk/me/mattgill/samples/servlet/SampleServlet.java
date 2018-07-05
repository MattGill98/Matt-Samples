package uk.me.mattgill.samples.servlet;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN_TYPE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/*")
public class SampleServlet extends HttpServlet {

    private static final long serialVersionUID = -1967914033651731155L;

	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.reset();
        resp.setContentType(TEXT_PLAIN_TYPE.withCharset(UTF_8.name()).toString());
        resp.getWriter().write("Hello World!");
    }

}