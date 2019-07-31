package uk.me.mattgill.samples.fault.tolerance.test.service;

import java.time.temporal.ChronoUnit;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

@Path("/fallback")
@RequestScoped
public class FallbackResource {

    @GET
    @Fallback(fallbackMethod = "fallback")
    @Timeout(value = 1, unit = ChronoUnit.NANOS)
    public String helloWorld() {
        return "Hello World!";
    }

    public String fallback() {
        return "Fellback!";
    }

}