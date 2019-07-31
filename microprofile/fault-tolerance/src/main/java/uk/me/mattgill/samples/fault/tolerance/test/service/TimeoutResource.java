package uk.me.mattgill.samples.fault.tolerance.test.service;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.logging.Level.WARNING;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.faulttolerance.Timeout;

@Path("/timeout")
@RequestScoped
public class TimeoutResource {

    private static Logger LOGGER = Logger.getLogger(TimeoutResource.class.getName());

    @GET
    @Timeout(value = 1, unit = SECONDS)
    public String helloWorld() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            LOGGER.log(WARNING, "Sleeping ended early!");
        }
        return "Hello World!";
    }

}